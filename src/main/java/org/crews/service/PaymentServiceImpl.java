package org.crews.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.TransferApiResponse;
import org.crews.dto.core.TransferRequest;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.dto.sqs.MessagePayload;
import org.crews.model.Account;
import org.crews.model.Card;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.crews.model.constants.CardName;
import org.crews.model.constants.PaymentTargetAccount;
import org.crews.repository.AccountRepository;
import org.crews.repository.CardRepository;
import org.crews.repository.MemberShipRepository;
import org.crews.utils.QRCodeUtil;
import org.crews.utils.S3Util;
import org.crews.utils.SQSUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final MemberShipRepository memberShipRepository;
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final QRCodeUtil qrCodeUtil;
    private final S3Util s3Util;
    private final CoreService coreService;
    private final SQSUtil sqsUtil;

    @Override
    public String generateQRCodeAndUpload(Long memberId, AgitInfoRequest agitInfoRequest) throws WriterException {
        Card card = cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No card found for the given member"));

        String qrData = String.format("http://localhost:8080/payments/execute?cardNumber=%s&expireDate=%s&memberId=%s",
                card.getCardNumber(), LocalDateTime.now().plusMinutes(1).plusSeconds(30), memberId);

        String folderPath = String.format("payments/%d/%d", memberId, agitInfoRequest.getAgitId());
        String s3Key = s3Util.generateUniqueFileName(folderPath, "qrCode.png");

        byte[] qrCodeBytes = qrCodeUtil.generateQRCode(qrData, 300, 300);

        s3Util.deleteAllFilesInFolder(folderPath); // 폴더 내 기존 파일 삭제
        s3Util.uploadToS3(qrCodeBytes, s3Key);

        return s3Util.generateCloudFrontUrl(s3Key);
    }

    public void processPayment(String cardNumber, String expireDate, Long memberId) {
        // 1. ExpireDate 검증
        LocalDateTime expirationTime = LocalDateTime.parse(expireDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (expirationTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("QR Code is expired");
        }

        // 2. Card 정보 확인
        Card card = cardRepository.findByCardNumberAndMemberId(cardNumber, memberId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found or not associated with the member"));

        if (card.isDeleted()) {
            throw new IllegalArgumentException("The card is no longer valid");
        }

        // 3. Account 정보 확인
        Account account = card.getAccount();
        if (account == null) {
            throw new IllegalArgumentException("No associated account found for the card");
        }

        TransferRequest transferRequest = TransferRequest.builder()
                .recvAccountNum(PaymentTargetAccount.TARGET_ACCOUNT.getTarget())
                .finUseNum(account.getFintecNumber())
                .description("카드 결제")
                .amt(BigDecimal.valueOf(10000))
                .build();
        TransferApiResponse transferApiResponse = coreService.transferFee(transferRequest);
        MessagePayload messagePayload = new MessagePayload(memberId, transferApiResponse.getData(), "Payment Request Success");
        sqsUtil.sendMessage(messagePayload);

    }

    @Override
    public List<PaymentInfoResponse> getPaymentInfo(Long memberId) {
        List<Membership> memberships = memberShipRepository.findByMemberIdAndAgitRoleNot(memberId, AgitRole.TEMP);
        List<PaymentInfoResponse> paymentInfoResponses = new ArrayList<>();

        for (Membership membership : memberships) {
            paymentInfoResponses.add(PaymentInfoResponse.builder()
                    .name(membership.getAgit().getAgitName())
                    .agitRole(membership.getAgitRole())
                    .agitId(membership.getAgit().getId())
                    .cardName(CardName.WOORI_CARD.getType())
                    .build());
        }

        return paymentInfoResponses;
    }
}
