package ma.apostorial.tmdl_backend.roulette.dtos;

import java.util.List;

import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelRouletteResponse;

public record RouletteResultResponse(
    int count,
    List<Integer> percentages,
    ClassicLevelRouletteResponse currentLevel,
    boolean completed,
    boolean givenUp
) { }
