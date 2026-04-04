package io.tradeflow.service.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.trace.Span;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.Map;

@ApplicationScoped
@Slf4j
public class EventConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    // TODO: rename channel to match your mp.messaging.incoming config
    @Incoming("your-topic-in")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public Uni<Void> onEvent(Message<String> message) {
        Map<String, Object> payload;
        try {
            payload = mapper.readValue(message.getPayload(), MAP_TYPE);
        } catch (Exception e) {
            log.error("JSON parse failed — discarding. error={}", e.getMessage());
            return Uni.createFrom().completionStage(message.ack());
        }

        String eventType = (String) payload.get("event_type");
        if (eventType == null) {
            return Uni.createFrom().completionStage(message.ack());
        }

        // Tag current OTel span
        Span.current()
            .setAttribute("messaging.system", "kafka")
            .setAttribute("messaging.operation", "receive")
            .setAttribute("event.type", eventType);

        log.info("Event received: type={}", eventType);

        // TODO: handle your events here
        return Uni.createFrom().completionStage(message.ack());
    }
}