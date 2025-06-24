package ma.apostorial.tmdl_backend.roulette.dtos;

import java.util.UUID;

import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelRouletteResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;

public record RouletteQueryResponse(
    UUID id,
    String name,
    PlayerResponse player,
    ClassicLevelRouletteResponse currentLevel,
    boolean completed,
    boolean givenUp
) { }
