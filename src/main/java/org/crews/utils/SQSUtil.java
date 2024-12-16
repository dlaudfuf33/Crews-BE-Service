package org.crews.utils;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.sqs.MessagePayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSUtil {
    @Value("${cloud.aws.sqs.queue.url}")
    private String url;

    private final AmazonSQS amazonSQS;
    public void sendMessage(Object messageObject) {

        try {
            // 객체를 JSON 형식으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
            String messageBody = objectMapper.writeValueAsString(messageObject);
            SendMessageRequest sendMessageRequest = new SendMessageRequest(url,
                    messageBody);
            amazonSQS.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e.getCause());
        }
    }

    public MessagePayload receiveAndDeleteMessages(Long targetMemberId) {
        try {
            // 메시지 요청 설정
            ReceiveMessageRequest receiveRequest = new ReceiveMessageRequest()
                    .withQueueUrl(url)
                    .withMaxNumberOfMessages(10)
                    .withWaitTimeSeconds(5);

            // 메시지 수신
            List<Message> messages = amazonSQS.receiveMessage(receiveRequest).getMessages();
            log.info("Received {} messages from SQS queue.", messages.size());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());


            for (Message message : messages) {
                try {
                    log.info("Processing message: {}", message.getBody());
                    MessagePayload payload = objectMapper.readValue(message.getBody(), MessagePayload.class);

                    if (payload.getMemberId() != null && payload.getMemberId().equals(targetMemberId)) {
                        log.info("Matching message found for memberId: {}", targetMemberId);

                        // 메시지 삭제
                        deleteMessage(message);
                        return payload;
                    }
                } catch (Exception e) {
                    log.error("Error while processing message: {}", message.getBody(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error while receiving messages from SQS queue.", e);
        }

        log.info("No matching messages found for memberId: {}", targetMemberId);
        return null;
    }

    private void deleteMessage(Message message) {
        try {
            amazonSQS.deleteMessage(new DeleteMessageRequest(url, message.getReceiptHandle()));
            log.info("Deleted message with receipt handle: {}", message.getReceiptHandle());
        } catch (Exception e) {
            log.error("Failed to delete message with receipt handle: {}", message.getReceiptHandle(), e);
        }
    }

}
