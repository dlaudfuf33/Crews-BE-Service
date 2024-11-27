package org.crews.utils;

import lombok.RequiredArgsConstructor;
import org.crews.service.MemberService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MessageScheduler {

    private final MemberService memberService;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteVerifyMessages() {
        memberService.deleteVerifyMessages();
    }
}
