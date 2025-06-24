package ma.apostorial.tmdl_backend.level.dtos.classic;

import ma.apostorial.tmdl_backend.level.enums.Difficulty;

public record ClassicLevelQueryResponse(
    String ingameId,
    String name,
    Difficulty difficulty,
    String publisher,
    int ranking
) { }
