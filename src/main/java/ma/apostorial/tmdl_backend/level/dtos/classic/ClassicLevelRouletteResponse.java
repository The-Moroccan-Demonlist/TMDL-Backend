package ma.apostorial.tmdl_backend.level.dtos.classic;

import java.util.UUID;

public record ClassicLevelRouletteResponse(
    UUID id,
    String ingameId,
    String name,
    int ranking,
    String thumbnailLink
) { }
