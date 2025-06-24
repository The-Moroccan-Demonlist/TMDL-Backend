package ma.apostorial.tmdl_backend.submission.services.internal.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.submission.entities.ClassicSubmission;
import ma.apostorial.tmdl_backend.submission.enums.Status;

public interface ClassicSubmissionInternalService {
    ClassicSubmission findById(UUID submissionId);
    Page<ClassicSubmission> query(String query, Status status, int page, int size);
    Page<ClassicSubmission> findByAuthenticatedPlayer(int page, int size, Jwt jwt);
}
