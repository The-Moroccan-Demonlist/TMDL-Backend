package ma.apostorial.tmdl_backend.record.dtos.platformer;

import java.time.Duration;

import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelDTOResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;

public record PlatformerRecordResponse(
    PlayerResponse player,
    PlatformerLevelDTOResponse level,
    String videoLink,
    Duration recordTime
) { }
