package ma.apostorial.tmdl_backend.player.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerUpdateRequest(
    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 255)
    String username,
    @NotBlank(message = "Discord cannot be empty.")
    @Size(max = 255)
    String discord,
    @NotBlank(message = "YouTube cannot be empty.")
    @Size(max = 255)
    String youtube,
    @NotBlank(message = "X cannot be empty.")
    @Size(max = 255)
    String twitter,
    @NotBlank(message = "Twitch cannot be empty.")
    @Size(max = 255)
    String twitch
) { }
