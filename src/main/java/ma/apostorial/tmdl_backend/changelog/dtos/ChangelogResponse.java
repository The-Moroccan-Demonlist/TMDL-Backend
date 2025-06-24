package ma.apostorial.tmdl_backend.changelog.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;

public record ChangelogResponse(
    UUID id,
    PlayerResponse author,
    String content,
    LocalDateTime publishedAt,
    boolean pinned
) { }
