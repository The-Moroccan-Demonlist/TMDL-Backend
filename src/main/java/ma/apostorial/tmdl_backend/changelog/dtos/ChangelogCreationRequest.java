package ma.apostorial.tmdl_backend.changelog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ma.apostorial.tmdl_backend.changelog.enums.Category;

public record ChangelogCreationRequest(
    @NotBlank(message = "Content cannot be empty.")
    @Size(max = 510)
    String content,
    @NotNull(message = "Category must be one of: CLASSIC, CLASSIC_PENDING, PLATFORMER, PLATFORMER_PENDING")
    Category category
) { }
