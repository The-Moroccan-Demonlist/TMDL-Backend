package ma.apostorial.tmdl_backend.badge.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BadgeReorderRequest(
    @NotNull(message = "ID must not be null.")
    UUID id,
    @PositiveOrZero(message = "Order must be zero or positive.")
    int order
) { }
