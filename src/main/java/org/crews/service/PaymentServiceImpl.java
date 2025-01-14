package org.crews.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.CardPaymentRequest;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.QRCodeUtil;
import org.crews.utils.S3Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final QRCodeUtil qrCodeUtil;
    private final S3Util s3Util;
    private final CoreService coreService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String generateQRCodeAndUpload(Long memberId, CardPaymentRequest cardPaymentRequest) throws WriterException {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(cardPaymentRequest.getPinNumber(), member.getPinNumber())) {
            throw new CustomException(ErrorCode.VERIFY_PIN_MISMATCH);
        }

        log.info("유저 확인 완료!");

        Card card = cardRepository.findByMemberIdAndAgitId(memberId, cardPaymentRequest.getAgitId())
                .orElseThrow(() -> new IllegalArgumentException("No card found for the given MemberId and AgitId."));

//        String qrData = String.format("http://crews-be-service-env.eba-hvrvbmgq.ap-northeast-2.elasticbeanstalk.com/payments/execute?cardNumber=%s&expireDate=%s&memberId=%s",
        String qrData = String.format("http://localhost:8080/payments/execute?cardNumber=%s&expireDate=%s&memberId=%s",
                AESUtil.decrypt(card.getCardNumber()), LocalDateTime.now().plusMinutes(1).plusSeconds(30), memberId);

        String folderPath = String.format("payments/%d/%d", memberId, cardPaymentRequest.getAgitId());
        log.info(qrData);
        log.info(folderPath);
        String s3Key = s3Util.generateUniqueFileName(folderPath, "qrCode.png");
        log.info(s3Key);
        byte[] qrCodeBytes = qrCodeUtil.generateQRCode(qrData, 300, 300);
        log.info(Arrays.toString(qrCodeBytes));
        s3Util.deleteAllFilesInFolder(folderPath); // 폴더 내 기존 파일 삭제
        s3Util.uploadToS3(qrCodeBytes, s3Key);

        return s3Util.generateCloudFrontUrl(s3Key);
    }

    @Override
    public void processPayment(String cardNumber, String expireDate, Long memberId) {

    }


    @Override
    public List<PaymentInfoResponse> getPaymentInfo(Long memberId) {
        List<Membership> memberships = membershipRepository.findByMemberIdAndAgitRoleNot(memberId, AgitRole.TEMP);
        List<PaymentInfoResponse> paymentInfoResponses = new ArrayList<>();

        for (Membership membership : memberships) {

            AgitAndAccount agitAndAccount = membership.getAgit().getAgitAndAccount();
            if (agitAndAccount != null) {
                Account account = agitAndAccount.getAccount();

                // Account가 존재하는 경우 Card 정보 조회
                if (account != null && !account.getCards().isEmpty()) {
                    for (Card card : account.getCards()) {
                        paymentInfoResponses.add(PaymentInfoResponse.builder()
                                .name(membership.getAgit().getAgitName())
                                .agitRole(membership.getAgitRole())
                                .agitId(membership.getAgit().getId())
                                .cardName(card.getCardName())
                                .cardCode("3475")
                                .src(card.getCardImage())
                                .build());
                    }
                } else {
                    // 카드가 없는 경우 기본 값 처리 (optional)
                    paymentInfoResponses.add(PaymentInfoResponse.builder()
                            .name(membership.getAgit().getAgitName())
                            .agitRole(membership.getAgitRole())
                            .agitId(membership.getAgit().getId())
                            .cardName("")
                            .cardCode("")
                            .src("")
                            .build());
                }
            } else {
                // Account가 없는 경우 기본 값 처리 (optional)
                paymentInfoResponses.add(PaymentInfoResponse.builder()
                        .name(membership.getAgit().getAgitName())
                        .agitRole(membership.getAgitRole())
                        .agitId(membership.getAgit().getId())
                        .cardName("")
                        .cardCode("")
                        .src("")
                        .build());
            }
        }

        return paymentInfoResponses;
    }

}
