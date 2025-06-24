package ma.apostorial.tmdl_backend.player.dtos;

import java.util.UUID;

public record PlayerResponse(
    UUID id,
    String username
) { }
