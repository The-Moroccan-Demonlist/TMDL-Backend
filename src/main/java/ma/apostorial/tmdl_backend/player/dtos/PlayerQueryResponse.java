package ma.apostorial.tmdl_backend.player.dtos;

import java.util.UUID;

public record PlayerQueryResponse(
    UUID id,
    String username,
    Double classicPoints,
    Double platformerPoints
) { }
