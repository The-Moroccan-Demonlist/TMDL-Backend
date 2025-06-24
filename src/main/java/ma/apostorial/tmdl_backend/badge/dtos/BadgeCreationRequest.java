package ma.apostorial.tmdl_backend.badge.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BadgeCreationRequest(
    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 255)
    String name,
    @NotBlank(message = "Description cannot be empty.")
    @Size(max = 255)
    String description,
    @NotBlank(message = "Hex Code cannot be empty.")
    @Pattern(regexp = "^[0-9A-Fa-f]{6}$", message = "Hex Code must be a valid 6-digit hexadecimal color.")
    String hexCode
) { }
