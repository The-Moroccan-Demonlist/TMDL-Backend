package ma.apostorial.tmdl_backend.level.dtos.platformer;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;

public record PlatformerLevelUpdateRequest(
    @NotBlank(message = "In-game cannot be empty.")
    @Pattern(regexp = "^[0-9]+$", message = "In-game id must contain only numbers.")
    @Size(max = 255)
    String ingameId,
    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 255)
    String name,
    @NotBlank(message = "Publisher cannot be empty.")
    @Size(max = 255)
    String publisher,
    @NotNull(message = "Difficulty must be one of: EASY_DEMON, MEDIUM_DEMON, HARD_DEMON, INSANE_DEMON, EXTREME_DEMON")
    Difficulty difficulty,
    @NotNull(message = "Video link cannot be empty.")
    @Size(max = 255)
    String videoLink,
    @NotNull(message = "Thumbnail link cannot be empty.")
    @Size(max = 255)
    String thumbnailLink,
    @NotNull(message = "Record holder cannot be empty.")
    UUID recordHolder
) { }
