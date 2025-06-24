package ma.apostorial.tmdl_backend.submission.services.implementations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.UnauthorizedException;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.PlatformerLevelInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.services.interfaces.PlatformerRecordService;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.entities.PlatformerSubmission;
import ma.apostorial.tmdl_backend.submission.enums.Status;
import ma.apostorial.tmdl_backend.submission.mappers.PlatformerSubmissionMapper;
import ma.apostorial.tmdl_backend.submission.repositories.PlatformerSubmissionRepository;
import ma.apostorial.tmdl_backend.submission.services.interfaces.PlatformerSubmissionService;
import ma.apostorial.tmdl_backend.submission.services.internal.interfaces.PlatformerSubmissionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class PlatformerSubmissionServiceImpl implements PlatformerSubmissionService {
    private final PlatformerSubmissionRepository platformerSubmissionRepository;
    private final PlatformerSubmissionInternalService platformerSubmissionInternalService;
    private final PlatformerLevelInternalService platformerLevelInternalService;
    private final PlayerInternalService playerInternalService;
    private final PlatformerSubmissionMapper platformerSubmissionMapper;
    private final PlatformerRecordService platformerRecordService;
    private final LogInternalService logInternalService;

    @Override
    public PlatformerSubmissionResponse create(PlatformerSubmissionCreationRequest request, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        PlatformerSubmission submission = platformerSubmissionMapper.fromCreationRequestToEntity(request);
        String ingameId = request.level();
        Optional<PlatformerLevel> optionalLevel = platformerLevelInternalService.findOptionalByIngameId(ingameId);
        if (optionalLevel.isPresent()) {
            submission.setLevel(optionalLevel.get());
            submission.setUnexistantIngameId(null);
        } else {
            submission.setLevel(null);
            submission.setUnexistantIngameId(ingameId);
        }

        submission.setPlayer(player);
        PlatformerSubmission savedSubmission = platformerSubmissionRepository.save(submission);
        return platformerSubmissionMapper.fromEntityToResponse(savedSubmission);
    }

    @Override
    public PlatformerSubmissionResponse findById(UUID submissionId) {
        PlatformerSubmission submission = platformerSubmissionInternalService.findById(submissionId);
        return platformerSubmissionMapper.fromEntityToResponse(submission);
    }

    @Override
    public Page<PlatformerSubmissionResponse> query(String query, Status status, int page, int size) {
        Page<PlatformerSubmission> submissions = platformerSubmissionInternalService.query(query, status, page, size);
        return submissions.map(platformerSubmissionMapper::fromEntityToResponse);
    }
    @Override
    public Page<PlatformerSubmissionResponse> findByAuthenticatedPlayer(int page, int size, Jwt jwt) {
        Page<PlatformerSubmission> submissions = platformerSubmissionInternalService.findByAuthenticatedPlayer(page, size, jwt);
        return submissions.map(platformerSubmissionMapper::fromEntityToResponse);
    }

    @Override
    public PlatformerSubmissionResponse update(UUID submissionId, PlatformerSubmissionUpdateRequest request, Jwt jwt) {
        PlatformerSubmission originalSubmission = platformerSubmissionInternalService.findById(submissionId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if(!originalSubmission.getPlayer().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to edit this submission.");
        }
        PlatformerSubmission submission = platformerSubmissionMapper.updateFromRequest(request, originalSubmission);
        return platformerSubmissionMapper.fromEntityToResponse(submission);
    }

    @Override
    public void deleteById(UUID submissionId, Jwt jwt) {
        PlatformerSubmission submission = platformerSubmissionInternalService.findById(submissionId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if(!submission.getPlayer().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this submission.");
        }
        platformerSubmissionRepository.deleteById(submissionId);
    }

    @Override
    public void changeStatus(UUID submissionId, Status status, Jwt jwt) {
        PlatformerSubmission submission = platformerSubmissionInternalService.findById(submissionId);
        Status originalStatus = submission.getStatus();
        LocalDateTime approvalDate = LocalDateTime.now();
        submission.setStatus(status);
        submission.setApprovalDate(approvalDate);
        if(status == Status.APPROVED) {
            PlatformerRecordCreationRequest request = new PlatformerRecordCreationRequest(
                submission.getPlayer().getId(),
                submission.getLevel().getId(),
                submission.getRecordTime(),
                submission.getVideoLink()
            );
            platformerRecordService.create(request, jwt);
        }
        logInternalService.create(Type.PLATFORMER_SUBMISSION, jwt, "Changed platformer submission [id={}] status from {} to {}.", submissionId, originalStatus, status);
        platformerSubmissionRepository.save(submission);
    }
    
}
