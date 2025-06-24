package ma.apostorial.tmdl_backend.player.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.player.entities.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
    boolean existsBySub(String sub);
    Optional<Player> findByEmail(String email);
    Optional<Player> findByUsername(String username);
    Optional<Player> findBySub(String sub);
    Page<Player> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    List<Player> findAllByRegionId(UUID regionId);
    // List<Player> findByIsStaffTrue();
    List<Player> findAllByClassicPointsGreaterThanOrderByClassicPointsDesc(Double minPoints);
    List<Player> findAllByPlatformerPointsGreaterThanOrderByPlatformerPointsDesc(Double minPoints);   
}
