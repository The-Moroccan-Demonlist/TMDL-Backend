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
import ma.apostorial.tmdl_backend.submission.entities.ClassicSubmission;
import ma.apostorial.tmdl_backend.submission.enums.Status;
import ma.apostorial.tmdl_backend.submission.repositories.ClassicSubmissionRepository;
import ma.apostorial.tmdl_backend.submission.services.internal.interfaces.ClassicSubmissionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class ClassicSubmissionInternalServiceImpl implements ClassicSubmissionInternalService {
    private final ClassicSubmissionRepository classicSubmissionRepository;
    private final PlayerInternalService playerInternalService;

    @Override
    public ClassicSubmission findById(UUID submissionId) {
        return classicSubmissionRepository.findById(submissionId).orElseThrow(() -> new EntityNotFoundException("Classic submission with id " + submissionId + " not found."));
    }

    @Override
    public Page<ClassicSubmission> query(String query, Status status, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ClassicSubmission> result = classicSubmissionRepository.query(query, status, pageable);
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }

    @Override
    public Page<ClassicSubmission> findByAuthenticatedPlayer(int page, int size, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassicSubmission> result = classicSubmissionRepository.findByPlayerIdOrderBySubmissionDateDesc(player.getId(), pageable);
        
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }
}
