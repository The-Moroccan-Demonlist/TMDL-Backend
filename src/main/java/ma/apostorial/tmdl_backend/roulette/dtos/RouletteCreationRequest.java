package ma.apostorial.tmdl_backend.roulette.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RouletteCreationRequest(
    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 255)
    String name,
    @NotNull(message = "Types cannot be empty.")
    List<String> types
) { }
