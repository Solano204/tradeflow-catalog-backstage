package io.tradeflow.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    // TODO: Replace topic and groupId with your values
    @KafkaListener(
            topics = "your.topic",
            groupId = "your-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onEvent(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) {
        Map<String, Object> payload = record.value();
        if (payload == null) {
            ack.acknowledge();
            return;
        }

        String eventType = (String) payload.get("event_type");
        log.info("Event received: type={}", eventType);

        try {
            // TODO: handle your events here
            switch (eventType != null ? eventType : "") {
                case "your.event" -> handleYourEvent(payload);
                default -> log.debug("Ignoring event type: {}", eventType);
            }
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed processing event type={}: {}", eventType, e.getMessage(), e);
            // Do NOT ack — Kafka will retry
        }
    }

    private void handleYourEvent(Map<String, Object> payload) {
        // TODO: implement
    }
}