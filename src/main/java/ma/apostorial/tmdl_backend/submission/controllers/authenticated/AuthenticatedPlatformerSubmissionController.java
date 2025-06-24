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
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.services.interfaces.PlatformerSubmissionService;

@RestController @RequiredArgsConstructor @RequestMapping("/api/authenticated/platformer-submissions")
public class AuthenticatedPlatformerSubmissionController {
    private final PlatformerSubmissionService platformerSubmissionService;

    @PostMapping("/create")
    public ResponseEntity<PlatformerSubmissionResponse> create(
            @Valid @RequestBody PlatformerSubmissionCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerSubmissionService.create(request, jwt), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<PlatformerSubmissionResponse>> findByAuthenticatedPlayer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerSubmissionService.findByAuthenticatedPlayer(page, size, jwt), HttpStatus.OK);
    }

    @PatchMapping("update/{submissionId}")
    public ResponseEntity<PlatformerSubmissionResponse> update(
            @PathVariable UUID submissionId,
            @Valid @RequestBody PlatformerSubmissionUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerSubmissionService.update(submissionId, request, jwt), HttpStatus.OK);    
    }

    @DeleteMapping("/delete/{submissionId}")
    public ResponseEntity<Void> delete(
        @PathVariable UUID submissionId,
        @AuthenticationPrincipal Jwt jwt) {
        platformerSubmissionService.deleteById(submissionId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }  
}
