package ma.apostorial.tmdl_backend.level.dtos.classic;

import java.util.UUID;

public record ClassicLevelDTOResponse(
    UUID id,
    String ingameId,
    String name
) { }
