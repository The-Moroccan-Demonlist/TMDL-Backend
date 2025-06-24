package ma.apostorial.tmdl_backend.submission.dtos.platformer;

import java.time.Duration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlatformerSubmissionCreationRequest(
    @NotNull(message = "Video link cannot be empty.")
    @Size(max = 255)
    String videoLink,
    @NotNull(message = "Raw footage cannot be empty.")
    @Size(max = 255)
    String rawFootage,
    @NotBlank(message = "Comment cannot be empty.")
    @Size(max = 255)
    String comment,
    @NotNull(message = "Level cannot be empty.")
    @Size(max = 255)
    String level,
    @NotNull(message = "Record time cannot be empty.")
    Duration recordTime
) { }
