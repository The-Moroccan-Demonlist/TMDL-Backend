package ma.apostorial.tmdl_backend.submission.controllers.authenticated;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.services.interfaces.ClassicSubmissionService;

@RestController @RequiredArgsConstructor @RequestMapping("/api/authenticated/classic-submissions")
public class AuthenticatedClassicSubmissionController {
    private final ClassicSubmissionService classicSubmissionService;

    @PostMapping("/create")
    public ResponseEntity<ClassicSubmissionResponse> create(
            @Valid @RequestBody ClassicSubmissionCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicSubmissionService.create(request, jwt), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ClassicSubmissionResponse>> findByAuthenticatedPlayer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicSubmissionService.findByAuthenticatedPlayer(page, size, jwt), HttpStatus.OK);
    }

    @PatchMapping("update/{submissionId}")
    public ResponseEntity<ClassicSubmissionResponse> update(
            @PathVariable UUID submissionId,
            @Valid @RequestBody ClassicSubmissionUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicSubmissionService.update(submissionId, request, jwt), HttpStatus.OK);    
    }

    @DeleteMapping("/delete/{submissionId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID submissionId,
            @AuthenticationPrincipal Jwt jwt) {
        classicSubmissionService.deleteById(submissionId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
