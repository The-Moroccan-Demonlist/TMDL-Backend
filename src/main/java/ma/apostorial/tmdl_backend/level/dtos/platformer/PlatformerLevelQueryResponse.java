package ma.apostorial.tmdl_backend.level.dtos.platformer;

import ma.apostorial.tmdl_backend.level.enums.Difficulty;

public record PlatformerLevelQueryResponse(
    String ingameId,
    String name,
    Difficulty difficulty,
    String publisher,
    int ranking
) { }
