package ma.apostorial.tmdl_backend.record.dtos.classic;

import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelDTOResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;

public record ClassicRecordResponse(
    PlayerResponse player,
    ClassicLevelDTOResponse level,
    String videoLink,
    int recordPercentage
) { }
