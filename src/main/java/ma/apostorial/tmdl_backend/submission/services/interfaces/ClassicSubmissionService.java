package ma.apostorial.tmdl_backend.submission.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.enums.Status;

public interface ClassicSubmissionService {
    ClassicSubmissionResponse create(ClassicSubmissionCreationRequest request, Jwt jwt);
    ClassicSubmissionResponse findById(UUID submissionId);
    Page<ClassicSubmissionResponse> query(String query, Status status, int page, int size);
    Page<ClassicSubmissionResponse> findByAuthenticatedPlayer(int page, int size, Jwt jwt);
    ClassicSubmissionResponse update(UUID submissionId, ClassicSubmissionUpdateRequest request, Jwt jwt);
    void deleteById(UUID submissionId, Jwt jwt);
    void changeStatus(UUID submissionId, Status status, Jwt jwt);    
}
