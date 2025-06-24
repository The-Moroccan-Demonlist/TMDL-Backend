package ma.apostorial.tmdl_backend.record.dtos.platformer;

import java.time.Duration;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlatformerRecordCreationRequest(
    @NotNull(message = "Player cannot be empty.")
    UUID player,
    @NotNull(message = "Level cannot be empty.")
    UUID level,
    @NotNull(message = "Record time cannot be empty.")
    Duration recordTime,
    @Size(max = 255)
    String videoLink
) { }
