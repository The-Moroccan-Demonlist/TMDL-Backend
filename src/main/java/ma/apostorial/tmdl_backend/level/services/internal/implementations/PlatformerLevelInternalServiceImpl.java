package ma.apostorial.tmdl_backend.level.services.internal.implementations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.repositories.PlatformerLevelRepository;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.PlatformerLevelInternalService;

@Service @Transactional @RequiredArgsConstructor
public class PlatformerLevelInternalServiceImpl implements PlatformerLevelInternalService {
    private final PlatformerLevelRepository platformerLevelRepository;
    
    @Override
    public PlatformerLevel findById(UUID levelId) {
        return platformerLevelRepository.findById(levelId).orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + levelId + " not found."));
    }

    @Override
    public boolean existsById(UUID levelId) {
        return platformerLevelRepository.existsById(levelId);
    }

    @Override
    public PlatformerLevel findByIngameId(String ingameId) {
        return platformerLevelRepository.findByIngameId(ingameId).orElseThrow(() -> new EntityNotFoundException("Platformer level with id " + ingameId + " not found."));
    }

    @Override
    public Optional<PlatformerLevel> findOptionalByIngameId(String ingameId) {
        return platformerLevelRepository.findByIngameId(ingameId);
    }

    @Override
    public List<PlatformerLevel> findAllById(List<UUID> levelIds) {
        return platformerLevelRepository.findAllById(levelIds);
    } 

    @Override
    public List<PlatformerLevel> query(String query, Difficulty difficulty, String type) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }
        List<PlatformerLevel> result = platformerLevelRepository.query(query, difficulty, type);

        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }

        return result;
    }
}
