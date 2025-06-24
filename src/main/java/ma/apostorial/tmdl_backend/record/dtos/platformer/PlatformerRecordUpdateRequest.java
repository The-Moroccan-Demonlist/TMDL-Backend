package ma.apostorial.tmdl_backend.record.dtos.platformer;

import java.time.Duration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlatformerRecordUpdateRequest(
    @NotNull(message = "Record time cannot be empty.")
    Duration recordTime,
    @Size(max = 255)
    String videoLink
) { }
