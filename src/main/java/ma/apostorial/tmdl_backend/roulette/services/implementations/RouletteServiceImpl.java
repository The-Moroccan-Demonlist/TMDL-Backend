package ma.apostorial.tmdl_backend.roulette.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.apostorial.tmdl_backend.common.exceptions.IllegalArgumentException;
import ma.apostorial.tmdl_backend.common.exceptions.UnauthorizedException;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.ClassicLevelInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteCreationRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteDoneRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResultResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteQueryResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteUpdateRequest;
import ma.apostorial.tmdl_backend.roulette.entities.Roulette;
import ma.apostorial.tmdl_backend.roulette.mappers.RouletteMapper;
import ma.apostorial.tmdl_backend.roulette.repositories.RouletteRepository;
import ma.apostorial.tmdl_backend.roulette.services.interfaces.RouletteService;
import ma.apostorial.tmdl_backend.roulette.services.internal.interfaces.RouletteInternalService;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class RouletteServiceImpl implements RouletteService {
    private final RouletteInternalService rouletteInternalService;
    private final RouletteRepository rouletteRepository;
    private final PlayerInternalService playerInternalService;
    private final ClassicLevelInternalService classicLevelInternalService;
    private final RouletteMapper rouletteMapper;
    
    @Override
    public RouletteResponse create(RouletteCreationRequest request, Jwt jwt) {
        List<String> types = request.types();

        if (types.size() == 1 && (types.contains("main") || types.contains("extended"))) {
            throw new IllegalArgumentException("Cannot create a roulette with less than 2 list types.");
        }
        List<ClassicLevel> classicLevels = classicLevelInternalService.rouletteRandomQuery(types);
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        ClassicLevel currentLevel = classicLevels.get(0);

        Roulette roulette = rouletteMapper.fromCreationRequestToEntity(request);
        roulette.setClassicLevels(classicLevels);
        roulette.setCurrentLevel(currentLevel);
        roulette.setPlayer(player);

        Roulette savedRoulette = rouletteRepository.save(roulette);

        return rouletteMapper.fromEntityToResponse(savedRoulette);
    }

    @Override
    public RouletteResponse findById(UUID rouletteId, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        Roulette roulette = rouletteInternalService.findById(rouletteId);
        if (roulette.getPlayer().getId().equals(player.getId())) {

        }
        return rouletteMapper.fromEntityToResponse(roulette);
    }

    @Override
    public List<RouletteQueryResponse> findAllByPlayer(Jwt jwt) {
        List<Roulette> roulettes = rouletteInternalService.findAllByPlayer(jwt);
        return roulettes.stream()
            .map(rouletteMapper::fromEntityToQueryResponse)
            .toList();
    }

    @Override
    public RouletteResponse update(UUID rouletteId, RouletteUpdateRequest request, Jwt jwt) {
        Roulette originaRoulette = rouletteInternalService.findById(rouletteId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if (!originaRoulette.getPlayer().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to update this roulette.");
        }
        Roulette roulette = rouletteMapper.updateFromRequest(request, originaRoulette);
        return rouletteMapper.fromEntityToResponse(roulette);
    }

    @Override
    public void deleteById(UUID rouletteId, Jwt jwt) {
        Roulette roulette = rouletteInternalService.findById(rouletteId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if (!roulette.getPlayer().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this roulette.");
        }
        rouletteRepository.deleteById(rouletteId);
    }

    @Override
    public RouletteResultResponse markDone(RouletteDoneRequest request, Jwt jwt) {
        Roulette roulette = rouletteInternalService.findById(request.id());
        if (roulette.isCompleted() || roulette.isGivenUp()) {
            throw new IllegalArgumentException("This roulette cannot be played anymore.");
        }
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        if (!roulette.getPlayer().getId().equals(player.getId())) {
            throw new UnauthorizedException("You are not authorized to progress in this roulette.");
        }
        int currentCount = roulette.getCount();
        List<Integer> currentPercentages = roulette.getPercentages();
        int playersPercentage = request.playersPercentage();
        if (request.playersPercentage() == 100) {
            currentCount++;
            roulette.setCount(currentCount);
            roulette.getPercentages().add(playersPercentage);
            roulette.setPercentages(roulette.getPercentages());
            // roulette.setCurrentLevel(roulette.getClassicLevels().get(roulette.getCount()));
            roulette.setCompleted(true);
        } else if (currentPercentages.getLast() == playersPercentage) {
            currentCount++;
            roulette.setCount(currentCount);
            roulette.getPercentages().add(playersPercentage + 1);
            roulette.setPercentages(roulette.getPercentages());
            roulette.setCurrentLevel(roulette.getClassicLevels().get(roulette.getCount()));
        } else if (currentPercentages.getLast() < playersPercentage) {
            currentCount++;
            roulette.setCount(currentCount);
            roulette.getPercentages().add(playersPercentage + 1);
            roulette.setPercentages(roulette.getPercentages());
            roulette.setCurrentLevel(roulette.getClassicLevels().get(roulette.getCount()));
        } else {
            throw new IllegalArgumentException("Your percentage must be equal or superior to the required percentage to continue.");
        }
        Roulette savedRoulette = rouletteRepository.save(roulette);
        return rouletteMapper.fromEntityToResultResponse(savedRoulette);
    }

    @Override
    public RouletteResultResponse giveUp(UUID rouletteId, Jwt jwt) {
        Roulette roulette = rouletteInternalService.findById(rouletteId);
        if (roulette.isCompleted() || roulette.isGivenUp()) {
            throw new IllegalArgumentException("This roulette cannot be played anymore.");
        }
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        if (!roulette.getPlayer().getId().equals(player.getId())) {
            throw new UnauthorizedException("You are not authorized to progress in this roulette.");
        }
        roulette.setGivenUp(true);
        Roulette savedRoulette = rouletteRepository.save(roulette);
        return rouletteMapper.fromEntityToResultResponse(savedRoulette);
    }

    @Override
    public RouletteResponse clone(UUID rouletteId, String name, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        Roulette rouletteToCopy = rouletteInternalService.findById(rouletteId);
        Roulette newRoulette = rouletteMapper.clone(rouletteToCopy);
        newRoulette.setName(name);
        newRoulette.setCurrentLevel(rouletteToCopy.getClassicLevels().get(0));
        newRoulette.setPlayer(player);
        Roulette savedRoulette = rouletteRepository.save(newRoulette);
        return rouletteMapper.fromEntityToResponse(savedRoulette);
    }
}
