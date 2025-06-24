package ma.apostorial.tmdl_backend.level.dtos.classic;

import java.util.List;

import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;

public record ClassicLevelResponse(
    String ingameId,
    String name,
    String publisher,
    Difficulty difficulty,
    Duration duration,
    String videoLink,
    String thumbnailLink,
    int minimumCompletion,
    int ranking,
    Double points,
    Double minimumPoints,
    PlayerResponse firstVictor,
    List<ClassicRecord> records
) { }
