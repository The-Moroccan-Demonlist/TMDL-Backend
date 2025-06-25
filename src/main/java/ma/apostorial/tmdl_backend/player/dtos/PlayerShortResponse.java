package ma.apostorial.tmdl_backend.player.dtos;

import java.util.List;
import java.util.UUID;

public record PlayerShortResponse(
    UUID id,
    String username,
    String avatar,
    List<String> permissions
) { }
