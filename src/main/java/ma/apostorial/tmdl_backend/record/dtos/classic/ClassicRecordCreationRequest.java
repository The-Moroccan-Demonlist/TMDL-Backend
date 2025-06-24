package ma.apostorial.tmdl_backend.record.dtos.classic;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassicRecordCreationRequest(
    @NotNull(message = "Player cannot be empty.")
    UUID player,
    @NotNull(message = "Level cannot be empty.")
    UUID level,
    @Min(value = 1, message = "Record percentage must be at least 1")
    @Max(value = 100, message = "Record percentage cannot exceed 100")
    int recordPercentage,
    @Size(max = 255)
    String videoLink
) { }
