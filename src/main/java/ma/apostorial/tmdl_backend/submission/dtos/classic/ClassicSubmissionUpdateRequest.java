package ma.apostorial.tmdl_backend.submission.dtos.classic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClassicSubmissionUpdateRequest(
    @NotBlank(message = "Comment cannot be empty.")
    @Size(max = 255)
    String comment
) { }
