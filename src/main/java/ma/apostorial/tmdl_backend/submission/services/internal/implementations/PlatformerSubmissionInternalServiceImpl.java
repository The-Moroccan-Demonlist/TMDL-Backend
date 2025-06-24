package ma.apostorial.tmdl_backend.submission.services.internal.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.submission.entities.PlatformerSubmission;
import ma.apostorial.tmdl_backend.submission.enums.Status;
import ma.apostorial.tmdl_backend.submission.repositories.PlatformerSubmissionRepository;
import ma.apostorial.tmdl_backend.submission.services.internal.interfaces.PlatformerSubmissionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class PlatformerSubmissionInternalServiceImpl implements PlatformerSubmissionInternalService {
    private final PlatformerSubmissionRepository platformerSubmissionRepository;;
    private final PlayerInternalService playerInternalService;
    
    @Override
    public PlatformerSubmission findById(UUID submissionId) {
        return platformerSubmissionRepository.findById(submissionId).orElseThrow(() -> new EntityNotFoundException("Platformer submission with id " + submissionId + " not found."));
    }

    @Override
    public Page<PlatformerSubmission> query(String query, Status status, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<PlatformerSubmission> result = platformerSubmissionRepository.query(query, status, pageable);
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }

    @Override
    public Page<PlatformerSubmission> findByAuthenticatedPlayer(int page, int size, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        Pageable pageable = PageRequest.of(page, size);
        Page<PlatformerSubmission> result = platformerSubmissionRepository.findByPlayerIdOrderBySubmissionDateDesc(player.getId(), pageable);
        
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }
}
