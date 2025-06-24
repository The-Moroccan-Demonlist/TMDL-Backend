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
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.ClassicLevelInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.services.interfaces.ClassicRecordService;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.entities.ClassicSubmission;
import ma.apostorial.tmdl_backend.submission.enums.Status;
import ma.apostorial.tmdl_backend.submission.mappers.ClassicSubmissionMapper;
import ma.apostorial.tmdl_backend.submission.repositories.ClassicSubmissionRepository;
import ma.apostorial.tmdl_backend.submission.services.interfaces.ClassicSubmissionService;
import ma.apostorial.tmdl_backend.submission.services.internal.interfaces.ClassicSubmissionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class ClassicSubmissionServiceImpl implements ClassicSubmissionService {
    private final ClassicSubmissionRepository classicSubmissionRepository;
    private final ClassicSubmissionInternalService classicSubmissionInternalService;
    private final ClassicLevelInternalService classicLevelInternalService;
    private final PlayerInternalService playerInternalService;
    private final ClassicSubmissionMapper classicSubmissionMapper;
    private final ClassicRecordService classicRecordService;
    private final LogInternalService logInternalService;

    @Override
    public ClassicSubmissionResponse create(ClassicSubmissionCreationRequest request, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        ClassicSubmission submission = classicSubmissionMapper.fromCreationRequestToEntity(request);
        String ingameId = request.level();
        Optional<ClassicLevel> optionalLevel = classicLevelInternalService.findOptionalByIngameId(ingameId);
        if (optionalLevel.isPresent()) {
            submission.setLevel(optionalLevel.get());
            submission.setUnexistantIngameId(null);
        } else {
            submission.setLevel(null);
            submission.setUnexistantIngameId(ingameId);
        }

        submission.setPlayer(player);
        ClassicSubmission savedSubmission = classicSubmissionRepository.save(submission);
        return classicSubmissionMapper.fromEntityToResponse(savedSubmission);
        }

    @Override
    public ClassicSubmissionResponse findById(UUID submissionId) {
        ClassicSubmission submission = classicSubmissionInternalService.findById(submissionId);
        return classicSubmissionMapper.fromEntityToResponse(submission);
    }

    @Override
    public Page<ClassicSubmissionResponse> query(String query, Status status, int page, int size) {
        Page<ClassicSubmission> submissions = classicSubmissionInternalService.query(query, status, page, size);
        return submissions.map(classicSubmissionMapper::fromEntityToResponse);
    }

    @Override
    public Page<ClassicSubmissionResponse> findByAuthenticatedPlayer(int page, int size, Jwt jwt) {
        Page<ClassicSubmission> submissions = classicSubmissionInternalService.findByAuthenticatedPlayer(page, size, jwt);
        return submissions.map(classicSubmissionMapper::fromEntityToResponse);
    }

    @Override
    public ClassicSubmissionResponse update(UUID submissionId, ClassicSubmissionUpdateRequest request, Jwt jwt) {
        ClassicSubmission originalSubmission = classicSubmissionInternalService.findById(submissionId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if(!originalSubmission.getPlayer().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to edit this submission.");
        }
        ClassicSubmission submission = classicSubmissionMapper.updateFromRequest(request, originalSubmission);
        return classicSubmissionMapper.fromEntityToResponse(submission);
    }

    @Override
    public void deleteById(UUID submissionId, Jwt jwt) {
        ClassicSubmission submission = classicSubmissionInternalService.findById(submissionId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if(!submission.getPlayer().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this submission.");
        }
        classicSubmissionRepository.deleteById(submissionId);
    }

    @Override
    public void changeStatus(UUID submissionId, Status status, Jwt jwt) {
        ClassicSubmission submission = classicSubmissionInternalService.findById(submissionId);
        Status originalStatus = submission.getStatus();
        LocalDateTime approvalDate = LocalDateTime.now();
        submission.setStatus(status);
        submission.setApprovalDate(approvalDate);
        if(status == Status.APPROVED) {
            ClassicRecordCreationRequest request = new ClassicRecordCreationRequest(
                submission.getPlayer().getId(),
                submission.getLevel().getId(),
                submission.getRecordPercentage(),
                submission.getVideoLink()
            );
            classicRecordService.create(request, jwt);
        }
        logInternalService.create(Type.CLASSIC_SUBMISSION, jwt, "Changed classic submission [id={}] status from {} to {}.", submissionId, originalStatus, status);
        classicSubmissionRepository.save(submission);
    }
    
}
