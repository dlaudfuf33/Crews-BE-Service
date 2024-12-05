package org.crews.service;

import org.crews.dto.response.PaymentInfoResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentInfoResponse> getPaymentInfo(Long memberId);
}
