package ma.apostorial.tmdl_backend.region.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegionCreationRequest(
    @NotBlank(message = "Name cannot be empty.")
    @Size(max = 255)
    String name
) { }
