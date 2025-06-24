package ma.apostorial.tmdl_backend.level.dtos.platformer;

import java.util.List;

import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;

public record PlatformerLevelResponse(
    String ingameId,
    String name,
    String publisher,
    Difficulty difficulty,
    String videoLink,
    String thumbnailLink,
    int ranking,
    Double points,
    PlayerResponse recordHolder,
    List<PlatformerRecord> records
) { }
