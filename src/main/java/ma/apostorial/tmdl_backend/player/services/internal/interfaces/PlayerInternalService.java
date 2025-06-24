package ma.apostorial.tmdl_backend.player.services.internal.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;

public interface PlayerInternalService {
    void saveAll(Set<Player> players);
    Player findById(UUID playerId);
    Player findByUsername(String username);
    boolean existsById(UUID playerId);
    Page<Player> query(String query, int page, int size);
    List<Player> findAllByClassicPoints();
    List<Player> findAllByPlatformerPoints();
    Player getAuthenticatedPlayer(Jwt jwt);
    // List<Player> findAllStaff();
    void addClassicPoints(ClassicRecord record);
    void removeClassicPoints(ClassicRecord record);
    void addPlatformerPoints(PlatformerRecord record);
    void removePlatformerPoints(PlatformerRecord record);
}
