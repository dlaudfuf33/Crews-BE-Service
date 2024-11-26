package org.crews.utils;

import lombok.extern.slf4j.Slf4j;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequestScope
public class MessageUtil {
    private static final String API_URL = "https://api.pushcut.io/YZq8IV-2dLeFeBm3ZEYlT/execute?shortcut=SendSms.class";
    private static final String API_KEY = "dxQoAEoDctRZFA2-s6jI6Kh6";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private MessageUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    public static void send(String number, String msg) {
        try{
            String payload = String.format("""
                {
                    "input": {
                        "phoneNumber": "%s",
                        "messageText": "%s"
                    }
                }
                """, number, msg);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("API-Key", API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                log.info("{} 번호로 문자 메세지가 발송되었습니다.", number);
            } else {
                throw new CustomException(ErrorCode.SEND_MESSAGE_FAILED);
            }
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted during HTTP request", e);
            throw new CustomException(ErrorCode.SEND_MESSAGE_FAILED, "Thread was interrupted");
        } catch (Exception e) {
            log.error("Failed to send message", e);
            throw new CustomException(ErrorCode.SEND_MESSAGE_FAILED, "An error occurred while sending the message");
        }
    }
}