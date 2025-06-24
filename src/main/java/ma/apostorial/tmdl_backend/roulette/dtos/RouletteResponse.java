package ma.apostorial.tmdl_backend.roulette.dtos;

import java.util.List;
import java.util.UUID;

import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelRouletteResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;

public record RouletteResponse(
    UUID id,
    String name,
    List<ClassicLevelRouletteResponse> classicLevels,
    PlayerResponse player,
    int count,
    boolean completed,
    boolean givenUp,
    ClassicLevelRouletteResponse currentLevel,
    List<Integer> percentages
) { }
