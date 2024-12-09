package org.crews.service;

import com.google.zxing.WriterException;
import org.crews.dto.request.CardPaymentRequest;
import org.crews.dto.response.PaymentInfoResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentInfoResponse> getPaymentInfo(Long memberId);

    String generateQRCodeAndUpload(Long memberId, CardPaymentRequest agitInfoRequest) throws WriterException;

    void processPayment(String cardNumber, String expireDate, Long memberId);
}
