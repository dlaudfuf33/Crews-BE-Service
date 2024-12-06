package org.crews.controller;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.service.PaymentService;
import org.crews.utils.AuthUtil;
import org.crews.utils.SQSUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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
    public ResponseEntity<Map<String, String>> createPaymentQRCode(@RequestBody AgitInfoRequest agitInfoRequest, HttpServletRequest request) {
        try {
            Long memberId = authUtil.getMemberId(request);
            String qrCodeUrl = paymentService.generateQRCodeAndUpload(memberId, agitInfoRequest);

            return ResponseEntity.ok(Map.of("qrCode", qrCodeUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/result")
    public ResponseEntity<String> resultPayment(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        sqsUtil.receiveAndDeleteMessages(memberId);
        return null;
    }

    @GetMapping("/excute")
    public ResponseEntity<String> excutePayment(HttpServletRequest request) {

        return null;
    }
}
