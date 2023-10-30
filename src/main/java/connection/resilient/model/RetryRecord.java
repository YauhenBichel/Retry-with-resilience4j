package connection.resilient.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record RetryRecord(UUID id, LocalDateTime timestamp, int httpCode, RetryStatus status) { }
