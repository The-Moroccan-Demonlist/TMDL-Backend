package ma.apostorial.tmdl_backend.roulette.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.roulette.entities.Roulette;

@Repository
public interface RouletteRepository extends JpaRepository<Roulette, UUID> {
    List<Roulette> findAllByPlayerId(UUID playerId);
}
