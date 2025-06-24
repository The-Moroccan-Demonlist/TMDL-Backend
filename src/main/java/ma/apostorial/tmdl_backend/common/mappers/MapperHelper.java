package ma.apostorial.tmdl_backend.common.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.ClassicLevelInternalService;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.PlatformerLevelInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;

@Component @RequiredArgsConstructor
public class MapperHelper {
    private final PlayerInternalService playerInternalService;
    private final ClassicLevelInternalService classicLevelInternalService;
    private final PlatformerLevelInternalService platformerLevelInternalService;

    public Player mapPlayer(UUID playerId) {
        return playerId != null ? playerInternalService.findById(playerId) : null;
    }

    public ClassicLevel mapClassicLevel(UUID levelId) {
        return levelId != null ? classicLevelInternalService.findById(levelId) : null;
    }

    public PlatformerLevel mapPlatformerLevel(UUID levelId) {
        return levelId != null ? platformerLevelInternalService.findById(levelId) : null;
    }
}
