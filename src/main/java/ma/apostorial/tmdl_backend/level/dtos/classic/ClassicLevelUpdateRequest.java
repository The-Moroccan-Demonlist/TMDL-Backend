package ma.apostorial.tmdl_backend.level.dtos.classic;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;

public record ClassicLevelUpdateRequest(
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
    @NotNull(message = "Duration must be one of: TINY, SHORT, MEDIUM, LONG, XL")
    Duration duration,
    @NotNull(message = "Video link cannot be empty.")
    @Size(max = 255)
    String videoLink,
    @NotNull(message = "Thumbnail link cannot be empty.")
    @Size(max = 255)
    String thumbnailLink,
    @Min(value = 1, message = "Minimum completion must be at least 1")
    @Max(value = 100, message = "Minimum completion cannot exceed 100")
    int minimumCompletion,
    @NotNull(message = "First victor cannot be empty.")
    UUID firstVictor
) { }
