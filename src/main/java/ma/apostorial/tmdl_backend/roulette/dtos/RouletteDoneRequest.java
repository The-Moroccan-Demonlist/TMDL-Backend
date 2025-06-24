package ma.apostorial.tmdl_backend.roulette.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RouletteDoneRequest(
    @NotNull(message = "ID cannot be empty.")
    UUID id,
    @NotNull(message = "Current level cannot be empty.")
    UUID currentLevel,
    @Min(value = 1, message = "Player's percentage must be at least 1")
    @Max(value = 100, message = "Player's percentage cannot exceed 100")
    int playersPercentage
) { }
