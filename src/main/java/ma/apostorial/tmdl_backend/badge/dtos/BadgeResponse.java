package ma.apostorial.tmdl_backend.badge.dtos;

import java.util.UUID;

public record BadgeResponse(
    UUID id,
    String name,
    String description,
    String hexCode
) { }
