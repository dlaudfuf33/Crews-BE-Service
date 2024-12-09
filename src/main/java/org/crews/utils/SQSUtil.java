package org.crews.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.sqs.MessagePayload;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Slf4j
@Component
public class SQSUtil {
    private final SqsClient sqsClient;
    private final String queueUrl;

    public SQSUtil() {
        sqsClient = SqsClient.builder()
                .region(Region.AP_NORTHEAST_2) // 적절한 리전을 설정하세요
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                .queueName("Crews-Service-Queue") // 대기열 이름 설정
                .build();

        CreateQueueResponse createQueueResponse = sqsClient.createQueue(createQueueRequest);
        queueUrl = createQueueResponse.queueUrl();
    }

    public void sendMessage(Object messageObject) {
        if (queueUrl == null) {
            throw new IllegalStateException("Queue URL is not initialized. Call createQueue() first.");
        }

        try {
            // 객체를 JSON 형식으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
            String messageBody = objectMapper.writeValueAsString(messageObject);

            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .delaySeconds(0)
                    .build();

            sqsClient.sendMessage(sendMsgRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    public MessagePayload receiveAndDeleteMessages(Long targetMemberId) {
        if (queueUrl == null) {
            throw new IllegalStateException("Queue URL is not initialized. Call createQueue() first.");
        }

        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록

        MessagePayload payload = null;
        for (Message message : messages) {
            try {
                log.info("Received message: " + message.body());
                payload = objectMapper.readValue(message.body(), MessagePayload.class);

                // memberId 조건 확인
                if (payload.getMemberId() != null && payload.getMemberId().equals(targetMemberId)) {
                    log.info("Matching message found for memberId: " + targetMemberId);

                    // 메시지 삭제
                    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build();
                    sqsClient.deleteMessage(deleteMessageRequest);

                    return payload; // 조건에 맞는 메시지 반환
                } else {
                    log.info("Message does not match memberId: " + targetMemberId);
                }
            } catch (Exception e) {
                log.error("Error while processing message: " + message.body(), e);
            }
        }

        return null; // 조건에 맞는 메시지가 없을 경우 null 반환
    }

}
