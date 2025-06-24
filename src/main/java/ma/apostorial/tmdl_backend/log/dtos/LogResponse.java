package ma.apostorial.tmdl_backend.log.dtos;

import java.time.LocalDateTime;

import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;

public record LogResponse(
    PlayerResponse author,
    String content,
    Type type,
    LocalDateTime date
) { }
