package ma.apostorial.tmdl_backend.submission.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.enums.Status;

public interface PlatformerSubmissionService {
    PlatformerSubmissionResponse create(PlatformerSubmissionCreationRequest request, Jwt jwt);
    PlatformerSubmissionResponse findById(UUID submissionId);
    Page<PlatformerSubmissionResponse> query(String query, Status status, int page, int size);
    Page<PlatformerSubmissionResponse> findByAuthenticatedPlayer(int page, int size, Jwt jwt);
    PlatformerSubmissionResponse update(UUID submissionId, PlatformerSubmissionUpdateRequest request, Jwt jwt);
    void deleteById(UUID submissionId, Jwt jwt);
    void changeStatus(UUID submissionId, Status status, Jwt jwt);   
}
