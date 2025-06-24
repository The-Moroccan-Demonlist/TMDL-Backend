package ma.apostorial.tmdl_backend.roulette.services.internal.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.roulette.entities.Roulette;
import ma.apostorial.tmdl_backend.roulette.repositories.RouletteRepository;
import ma.apostorial.tmdl_backend.roulette.services.internal.interfaces.RouletteInternalService;

@Service @Transactional @RequiredArgsConstructor
public class RouletteInternalServiceImpl implements RouletteInternalService {
    private final RouletteRepository rouletteRepository;
    private final PlayerInternalService playerInternalService;

    @Override
    public Roulette findById(UUID rouletteId) {
        return rouletteRepository.findById(rouletteId).orElseThrow(() -> new EntityNotFoundException("Roulette with id " + rouletteId + " not found."));
    }

    @Override
    public List<Roulette> findAllByPlayer(Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        List<Roulette> result = rouletteRepository.findAllByPlayerId(player.getId());
        if (result.isEmpty()) {
            throw new NoContentException("No results for player with id: " + player.getId());
        }

        return result;
    }
}
