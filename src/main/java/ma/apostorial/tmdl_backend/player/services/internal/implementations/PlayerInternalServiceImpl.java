package ma.apostorial.tmdl_backend.player.services.internal.implementations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.repositories.PlayerRepository;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.region.services.internal.interfaces.RegionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class PlayerInternalServiceImpl implements PlayerInternalService {
    private final PlayerRepository playerRepository;
    private final RegionInternalService regionInternalService;

    @Override
    public void saveAll(Set<Player> players) {
        playerRepository.saveAll(players);
    }    

    @Override
    public Player findById(UUID playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new EntityNotFoundException("Player with id " + playerId + " not found."));
    }

    @Override
    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Player with username " + username + " not found."));
    }

    @Override
    public boolean existsById(UUID playerId) {
        return playerRepository.existsById(playerId);
    }

    @Override
    public Page<Player> query(String query, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Player> result;
         if (StringUtils.hasText(query)) {
            result = playerRepository.findByUsernameContainingIgnoreCase(query, pageable);
        } else {
            result = playerRepository.findAll(pageable);
        }

        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }

        return result;
    }

    @Override
    public List<Player> findAllByClassicPoints() {
        return Optional.of(playerRepository.findAllByClassicPointsGreaterThanOrderByClassicPointsDesc(0.0))
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new NoContentException("No result."));
    }

    @Override
    public List<Player> findAllByPlatformerPoints() {
        return Optional.of(playerRepository.findAllByPlatformerPointsGreaterThanOrderByPlatformerPointsDesc(0.0))
            .filter(list -> !list.isEmpty())
            .orElseThrow(() -> new NoContentException("No result."));
    }

    @Override
    public Player getAuthenticatedPlayer(Jwt jwt) {
        String sub = jwt.getSubject();
        return playerRepository.findBySub(sub).orElseThrow(() -> new EntityNotFoundException("Player with sub " + sub + " not found."));
    }

    // @Override
    // public List<Player> findAllStaff() {
    //     return Optional.of(playerRepository.findByIsStaffTrue())
    //         .filter(list -> !list.isEmpty())
    //         .orElseThrow(() -> new NoContentException("No result."));
    // }

    @Override
    public void addClassicPoints(ClassicRecord record) {
        Player player = record.getPlayer();
        ClassicLevel level = record.getLevel();
        if (record.getRecordPercentage() == 100) {
            player.setClassicPoints(player.getClassicPoints() + level.getPoints());
        } else {
            player.setClassicPoints(player.getClassicPoints() + level.getMinimumPoints());
        }
        regionInternalService.addClassicPoints(record);
        playerRepository.save(player);
    }

    @Override
    public void removeClassicPoints(ClassicRecord record) {
        Player player = record.getPlayer();
        ClassicLevel level = record.getLevel();
        if (record.getRecordPercentage() == 100) {
            player.setClassicPoints(player.getClassicPoints() - level.getPoints());
        } else {
            player.setClassicPoints(player.getClassicPoints() - level.getMinimumPoints());
        }
        regionInternalService.removeClassicPoints(record);
        playerRepository.save(player);
    }

    @Override
    public void addPlatformerPoints(PlatformerRecord record) {
        Player player = record.getPlayer();
        PlatformerLevel level = record.getLevel();
        player.setPlatformerPoints(player.getPlatformerPoints() + level.getPoints());
        regionInternalService.addPlatformerPoints(record);
        playerRepository.save(player);
    }

    @Override
    public void removePlatformerPoints(PlatformerRecord record) {
        Player player = record.getPlayer();
        PlatformerLevel level = record.getLevel();
        player.setPlatformerPoints(player.getPlatformerPoints() - level.getPoints());
        regionInternalService.removePlatformerPoints(record);
        playerRepository.save(player);
    }
}
