package ma.apostorial.tmdl_backend.level.services.internal.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;

public interface ClassicLevelInternalService {
    ClassicLevel findById(UUID levelId);
    boolean existsById(UUID levelId);
    ClassicLevel findByIngameId(String ingameId);
    Optional<ClassicLevel> findOptionalByIngameId(String ingameId);
    List<ClassicLevel> findAllById(List<UUID> levelIds);
    List<ClassicLevel> query(String query, Difficulty difficulty, Duration duration, String type);
    List<ClassicLevel> rouletteRandomQuery(List<String> types);
}
