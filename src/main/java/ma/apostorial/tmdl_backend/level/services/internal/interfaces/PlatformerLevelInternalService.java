package ma.apostorial.tmdl_backend.level.services.internal.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;

public interface PlatformerLevelInternalService {
    PlatformerLevel findById(UUID levelId);
    boolean existsById(UUID levelId);
    PlatformerLevel findByIngameId(String ingameId);
    Optional<PlatformerLevel> findOptionalByIngameId(String ingameId);
    List<PlatformerLevel> findAllById(List<UUID> levelIds);
    List<PlatformerLevel> query(String query, Difficulty difficulty, String type);
}
