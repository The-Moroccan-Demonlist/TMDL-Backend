package ma.apostorial.tmdl_backend.roulette.services.internal.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.roulette.entities.Roulette;

public interface RouletteInternalService {
    Roulette findById(UUID rouletteId);
    List<Roulette> findAllByPlayer(Jwt jwt);
}
