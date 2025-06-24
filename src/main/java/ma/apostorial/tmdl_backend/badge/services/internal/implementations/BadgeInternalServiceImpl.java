package ma.apostorial.tmdl_backend.badge.services.internal.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.badge.entities.Badge;
import ma.apostorial.tmdl_backend.badge.repositories.BadgeRepository;
import ma.apostorial.tmdl_backend.badge.services.internal.interfaces.BadgeInternalService;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;

@Service @Transactional @RequiredArgsConstructor
public class BadgeInternalServiceImpl implements BadgeInternalService {
    private final BadgeRepository badgeRepository;
    
    @Override
    public Badge findById(UUID badgeId) {
        return badgeRepository.findById(badgeId).orElseThrow(() -> new EntityNotFoundException("Badge with id " + badgeId + " not found."));
    }

    @Override
    public List<Badge> findAll() {
        List<Badge> result = badgeRepository.findAll();
        if (result.isEmpty()) {
            throw new NoContentException("No result.");
        }
        
        return result;
    }

    @Override
    public List<Badge> findAllById(List<UUID> badgeIds) {
        List<Badge> result = badgeRepository.findAllById(badgeIds);
        if (result.isEmpty()) {
            throw new NoContentException("No result.");
        }
        
        return result;
    }
}
