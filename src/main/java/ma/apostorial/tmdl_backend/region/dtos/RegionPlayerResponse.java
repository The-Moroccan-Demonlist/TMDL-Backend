package ma.apostorial.tmdl_backend.region.dtos;

import java.util.UUID;

public record RegionPlayerResponse(
    UUID id,
    String name
) { }
