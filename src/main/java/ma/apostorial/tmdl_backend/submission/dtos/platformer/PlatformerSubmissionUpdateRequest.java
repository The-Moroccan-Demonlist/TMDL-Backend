package ma.apostorial.tmdl_backend.submission.dtos.platformer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlatformerSubmissionUpdateRequest(
    @NotBlank(message = "Comment cannot be empty.")
    @Size(max = 255)
    String comment
) { }
