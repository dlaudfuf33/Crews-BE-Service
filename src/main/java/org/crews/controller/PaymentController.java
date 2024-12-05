package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.service.PaymentService;
import org.crews.utils.AuthUtil;
import org.crews.utils.SQSUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final AuthUtil authUtil;
    private final PaymentService paymentService;
    private final SQSUtil sqsUtil;

    @GetMapping
    public ResponseEntity<List<PaymentInfoResponse>> paymentInfo(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        List<PaymentInfoResponse> paymentInfoList = paymentService.getPaymentInfo(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(paymentInfoList);
    }

    @PostMapping
    public ResponseEntity<String> requestPayment(HttpServletRequest request) {
        authUtil.getMemberId(request);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @GetMapping("/result")
    public ResponseEntity<String> resultPayment() {
        sqsUtil.receiveAndDeleteMessages(1);
        return null;
    }
}
