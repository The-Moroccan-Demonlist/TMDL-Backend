package ma.apostorial.tmdl_backend.roulette.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RouletteUpdateRequest(
    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 255)
    String name
) { }
