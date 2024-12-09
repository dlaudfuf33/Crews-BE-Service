package org.crews.controller;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.crews.dto.core.TransferResponse;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.dto.request.CardPaymentRequest;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.dto.sqs.MessagePayload;
import org.crews.service.PaymentService;
import org.crews.utils.AuthUtil;
import org.crews.utils.SQSUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<Map<String, String>> createPaymentQRCode(@RequestBody CardPaymentRequest cardPaymentRequest, HttpServletRequest request) {
        try {
            Long memberId = authUtil.getMemberId(request);
            String qrCodeUrl = paymentService.generateQRCodeAndUpload(memberId, cardPaymentRequest);
            log.info(qrCodeUrl);
            if(qrCodeUrl.equals("wrong pin number")) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("qrCode", qrCodeUrl));

            }

            return ResponseEntity.ok(Map.of("qrCode", qrCodeUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/result")
    public ResponseEntity<MessagePayload> resultPayment(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        log.info(String.valueOf(memberId));
        MessagePayload payload = sqsUtil.receiveAndDeleteMessages(memberId);

        if (payload == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(MessagePayload.builder().memberId(memberId).message("Payment Result Not Found").build());
        } else {
            return ResponseEntity.ok(payload);
        }
    }


    @GetMapping("/execute")
    public ResponseEntity<String> executePayment(
            @RequestParam String cardNumber,
            @RequestParam String expireDate,
            @RequestParam Long memberId) {
        try {
            paymentService.processPayment(cardNumber, expireDate, memberId);
            return ResponseEntity.ok().body("");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
}
