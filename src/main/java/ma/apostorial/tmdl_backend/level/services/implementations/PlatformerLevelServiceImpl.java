package ma.apostorial.tmdl_backend.level.services.implementations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.mappers.PlatformerLevelMapper;
import ma.apostorial.tmdl_backend.level.repositories.PlatformerLevelRepository;
import ma.apostorial.tmdl_backend.level.services.interfaces.PlatformerLevelService;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.PlatformerLevelInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.region.entities.Region;
import ma.apostorial.tmdl_backend.region.services.internal.interfaces.RegionInternalService;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class PlatformerLevelServiceImpl implements PlatformerLevelService {
    private final RegionInternalService regionInternalService;
    private final PlayerInternalService playerInternalService;
    private final PlatformerLevelInternalService platformerLevelInternalService;
    private final PlatformerLevelRepository platformerLevelRepository;
    private final PlatformerLevelMapper platformerLevelMapper;
    private final LogInternalService logInternalService;

    public PlatformerLevelResponse createtest(PlatformerLevelCreationRequest request) {
        PlatformerLevel level = platformerLevelMapper.fromCreationRequestToEntity(request);
        level.setRanking(-1);
        level.calculatePoints();
        PlatformerLevel savedLevel = platformerLevelRepository.save(level);
        // logInternalService.create(Type.PLATFORMER_LEVEL, "Created platformer level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
        return platformerLevelMapper.fromEntityToResponse(savedLevel);
    }

    @Override
    public PlatformerLevelResponse create(PlatformerLevelCreationRequest request, Jwt jwt) {
        PlatformerLevel levelToCreate = platformerLevelMapper.fromCreationRequestToEntity(request);
        levelToCreate.calculatePoints();
        PlatformerLevel savedLevel = platformerLevelRepository.save(levelToCreate);

        List<PlatformerLevel> affectedLevels = platformerLevelRepository.findByRankingGreaterThanEqualOrderByRanking(savedLevel.getRanking());
        List<PlatformerRecord> recordsToCheck = new ArrayList<>();
        if (affectedLevels.size() <= 1) {
            logInternalService.create(Type.PLATFORMER_LEVEL, jwt, "Created platformer level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
            return platformerLevelMapper.fromEntityToResponse(savedLevel);
        }
        for (PlatformerLevel level : affectedLevels) {
            if (!level.getId().equals(savedLevel.getId())) {
                level.setRanking(level.getRanking() + 1);
            }
            if (level.getRanking() > 150) {
                level.setOldPoints(level.getPoints());
                level.setPoints(0.0);
                level.calculatePoints();
                recordsToCheck.addAll(level.getRecords());
                continue;
            }
            if (level.getId().equals(savedLevel.getId())) {
                continue;
            }
            level.setOldPoints(level.getPoints());
            level.calculatePoints();
            recordsToCheck.addAll(level.getRecords());
        }
        if (recordsToCheck.isEmpty()) {
            logInternalService.create(Type.PLATFORMER_LEVEL, jwt, "Created platformer level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
            return platformerLevelMapper.fromEntityToResponse(savedLevel);
        }

        for (PlatformerRecord record : recordsToCheck) {
            PlatformerLevel level = record.getLevel();
            Player player = record.getPlayer();
            Region region = player.getRegion();

            Double oldValue = level.getOldPoints();
            Double newValue = level.getPoints();

            Double oldPlayerPoints = player.getPlatformerPoints();
            Double newPlayerPoints = oldPlayerPoints - oldValue + newValue;

            Double oldRegionPoints = region.getPlatformerPoints();
            Double newRegionPoints = oldRegionPoints - oldValue + newValue;

            if (Double.compare(oldPlayerPoints, newPlayerPoints) != 0) {
                player.setPlatformerPoints(newPlayerPoints);
            }

            if (Double.compare(oldRegionPoints, newRegionPoints) != 0) {
                region.setPlatformerPoints(newRegionPoints);
            }
        }

        platformerLevelRepository.saveAll(affectedLevels);
        logInternalService.create(Type.PLATFORMER_LEVEL, jwt, "Created platformer level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
        return platformerLevelMapper.fromEntityToResponse(savedLevel);
    }

    @Override
    public PlatformerLevelResponse findById(UUID levelId) {
        PlatformerLevel level = platformerLevelInternalService.findById(levelId);
        return platformerLevelMapper.fromEntityToResponse(level);
    }

    @Override
    public PlatformerLevelResponse findByIngameId(String ingameId) {
        PlatformerLevel level = platformerLevelInternalService.findByIngameId(ingameId);
        return platformerLevelMapper.fromEntityToResponse(level);
    }

    @Override
    public PlatformerLevelResponse update(UUID levelId, PlatformerLevelUpdateRequest request, Jwt jwt) {
        PlatformerLevel originalLevel = platformerLevelInternalService.findById(levelId);
        String originalName = originalLevel.getName();
        PlatformerLevel level = platformerLevelMapper.updateFromRequest(request, originalLevel);
        logInternalService.create(Type.PLATFORMER_LEVEL, jwt, "Updated platformer level [name={}, id={}].", originalName, levelId);
        return platformerLevelMapper.fromEntityToResponse(level);
    }

    @Override
    public List<PlatformerLevelQueryResponse> query(String query, Difficulty difficulty, String type) {
        List<PlatformerLevel> levels = platformerLevelInternalService.query(query, difficulty, type);
        return levels.stream()
            .map(platformerLevelMapper::fromEntityToQueryResponse)
            .toList();
    }

    @Override
    public void deleteById(UUID levelId, Jwt jwt) {
        PlatformerLevel level = platformerLevelInternalService.findById(levelId);
        Set<Player> players = new HashSet<>();
        Set<Region> regions = new HashSet<>();
        for (PlatformerRecord record : level.getRecords()) {
            Player player = record.getPlayer();
            Region region = player.getRegion();

            Double value = level.getPoints();

            player.setClassicPoints(player.getClassicPoints() - value);
            region.setClassicPoints(region.getClassicPoints() - value);
            players.add(player);
            regions.add(region);
        }
        playerInternalService.saveAll(players);
        regionInternalService.saveAll(regions);
        platformerLevelRepository.deleteById(levelId);
        logInternalService.create(Type.PLATFORMER_LEVEL, jwt, "Deleted platformer level [name={}, id={}].", level.getName(), levelId);
    }
}
