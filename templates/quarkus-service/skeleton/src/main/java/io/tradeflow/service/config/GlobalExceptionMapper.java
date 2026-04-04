package io.tradeflow.service.config;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;

@Provider
@Slf4j
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof jakarta.ws.rs.WebApplicationException wae) {
            return wae.getResponse();
        }
        log.error("Unhandled: {}", e.getMessage(), e);
        return Response.status(500)
                .entity(Map.of(
                        "error", "INTERNAL_ERROR",
                        "message", "An unexpected error occurred",
                        "status", 500,
                        "timestamp", Instant.now().toString()
                ))
                .build();
    }
}