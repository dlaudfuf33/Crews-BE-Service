package org.crews.service;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.model.Card;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.crews.model.constants.CardName;
import org.crews.repository.CardRepository;
import org.crews.repository.MemberShipRepository;
import org.crews.utils.QRCodeUtil;
import org.crews.utils.S3Util;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final MemberShipRepository memberShipRepository;
    private final CardRepository cardRepository;
    private final QRCodeUtil qrCodeUtil;
    private final S3Util s3Util;

    @Override
    public String generateQRCodeAndUpload(Long memberId, AgitInfoRequest agitInfoRequest) throws WriterException {
        Card card = cardRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("No card found for the given member"));

        String qrData = String.format("http://localhost:8080/payments/excute?cardNumber=%s&expireDate=%s",
                card.getCardNumber(), LocalDateTime.now().plusMinutes(1).plusSeconds(30));

        String folderPath = String.format("payments/%d/%d", memberId, agitInfoRequest.getAgitId());
        String s3Key = s3Util.generateUniqueFileName(folderPath, "qrCode.png");

        byte[] qrCodeBytes = qrCodeUtil.generateQRCode(qrData, 300, 300);

        s3Util.deleteAllFilesInFolder(folderPath); // 폴더 내 기존 파일 삭제
        s3Util.uploadToS3(qrCodeBytes, s3Key);

        return s3Util.generateCloudFrontUrl(s3Key);
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
