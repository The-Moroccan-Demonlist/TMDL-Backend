package ma.apostorial.tmdl_backend.roulette.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.roulette.dtos.RouletteCreationRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteDoneRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResultResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteQueryResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteUpdateRequest;

public interface RouletteService {
    RouletteResponse create(RouletteCreationRequest request, Jwt jwt);
    RouletteResponse findById(UUID rouletteId, Jwt jwt);
    List<RouletteQueryResponse> findAllByPlayer(Jwt jwt);
    RouletteResponse update(UUID rouletteId, RouletteUpdateRequest request, Jwt jwt);
    void deleteById(UUID rouletteId, Jwt jwt);
    RouletteResultResponse markDone(RouletteDoneRequest request, Jwt jwt);
    RouletteResultResponse giveUp(UUID rouletteId, Jwt jwt);
    RouletteResponse clone(UUID rouletteId, String name, Jwt jwt);
}
