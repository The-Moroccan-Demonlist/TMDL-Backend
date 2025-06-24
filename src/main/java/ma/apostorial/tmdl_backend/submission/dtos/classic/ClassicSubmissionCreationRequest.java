package ma.apostorial.tmdl_backend.submission.dtos.classic;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassicSubmissionCreationRequest(
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
    @Min(value = 1, message = "Minimum completion must be at least 1")
    @Max(value = 100, message = "Minimum completion cannot exceed 100")
    int recordPercentage
) { }
