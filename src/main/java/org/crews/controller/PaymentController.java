package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.service.PaymentService;
import org.crews.utils.AuthUtil;
import org.crews.utils.SQSUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final AuthUtil authUtil;
    private final PaymentService paymentService;
    private final SQSUtil sqsUtil;



    @PostMapping
    public ResponseEntity<String> requestPayment(@RequestBody String test) {
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @GetMapping("/result")
    public ResponseEntity<String> resultPayment() {
        sqsUtil.receiveAndDeleteMessages(1);
        return null;
    }
}
