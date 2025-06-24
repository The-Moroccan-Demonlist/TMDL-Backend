package ma.apostorial.tmdl_backend.level.dtos.platformer;

import java.util.UUID;

public record PlatformerLevelDTOResponse(
    UUID id,
    String ingameId,
    String name
) { }
