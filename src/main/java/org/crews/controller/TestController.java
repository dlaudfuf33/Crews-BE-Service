package org.crews.controller;

import lombok.RequiredArgsConstructor;
import org.crews.dto.core.MemberToCoreRequest;
import org.crews.service.CoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final CoreService coreService;

    /**
     * 블로킹 방식으로 API 호출 수행
     * 해당 메소드는 동기적으로 호출되며, 응답을 받을 때까지 현재 스레드를 대기시킵니다.
     *
     * @return
     */
    @PostMapping("/block")
    public String callTestApiBlock() {
        MemberToCoreRequest memberToCoreRequest = new MemberToCoreRequest();
        memberToCoreRequest.setName("홍길동");
        memberToCoreRequest.setPhoneNumber("010-1234-5678");
        return coreService.postTestBlocking(memberToCoreRequest); // API 호출
    }

    /**
     * 논블로킹 방식으로 API 호출을 수행
     * 해당 메소드는 비동기적으로 호출되며, Mono 객체를 반환하여 이후 처리될 수 있도록 합니다.
     *
     * @return
     */
    @PostMapping("/nonblock")
    public Mono<String> callTestApiNonBlock() {
        MemberToCoreRequest
                memberToCoreRequest = new MemberToCoreRequest();
        memberToCoreRequest.setName("홍길동");
        memberToCoreRequest.setPhoneNumber("010-1234-5678");
        return coreService.postTestNonBlocking(memberToCoreRequest);
    }

}