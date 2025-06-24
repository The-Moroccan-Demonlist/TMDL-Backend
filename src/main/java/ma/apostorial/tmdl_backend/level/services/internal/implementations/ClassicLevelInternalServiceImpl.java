package ma.apostorial.tmdl_backend.level.services.internal.implementations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.level.repositories.ClassicLevelRepository;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.ClassicLevelInternalService;

@Service @Transactional @RequiredArgsConstructor
public class ClassicLevelInternalServiceImpl implements ClassicLevelInternalService {
    private final ClassicLevelRepository classicLevelRepository;
    
    @Override
    public ClassicLevel findById(UUID levelId) {
        return classicLevelRepository.findById(levelId).orElseThrow(() -> new EntityNotFoundException("Classic level with id " + levelId + " not found."));
    }

    @Override
    public boolean existsById(UUID levelId) {
        return classicLevelRepository.existsById(levelId);
    }

    @Override
    public ClassicLevel findByIngameId(String ingameId) {
        return classicLevelRepository.findByIngameId(ingameId).orElseThrow(() -> new EntityNotFoundException("Classic level with id " + ingameId + " not found."));
    }

    @Override
    public Optional<ClassicLevel> findOptionalByIngameId(String ingameId) {
        return classicLevelRepository.findByIngameId(ingameId);
    }

    @Override
    public List<ClassicLevel> findAllById(List<UUID> levelIds) {
        return classicLevelRepository.findAllById(levelIds);
    } 

    @Override
    public List<ClassicLevel> query(String query, Difficulty difficulty, Duration duration, String type) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }
        List<ClassicLevel> result = classicLevelRepository.query(query, difficulty, duration, type);

        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }

        return result;
    }

    @Override
    public List<ClassicLevel> rouletteRandomQuery(List<String> types) {
        if (types == null || types.isEmpty()) {
            types = List.of("main", "extended", "legacy");
        }

        if (types.size() == 1 && (types.contains("main") || types.contains("extended"))) {
            throw new IllegalArgumentException("Cannot create a roulette with less than 2 list types.");
        }

        boolean includeMain = types.contains("main");
        boolean includeExtended = types.contains("extended");
        boolean includeLegacy = types.contains("legacy");

        List<ClassicLevel> result = classicLevelRepository.rouletteRandomQuery(includeMain, includeExtended, includeLegacy);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("No levels found for the given types");
        }

        return result;
    }
}
