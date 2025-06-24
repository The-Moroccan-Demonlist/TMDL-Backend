package ma.apostorial.tmdl_backend.changelog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangelogUpdateRequest(
    @NotBlank(message = "Content cannot be empty.")
    @Size(max = 510)
    String content
) { }
