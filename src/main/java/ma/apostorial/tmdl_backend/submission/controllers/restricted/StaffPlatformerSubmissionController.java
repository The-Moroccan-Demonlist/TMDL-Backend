package ma.apostorial.tmdl_backend.submission.controllers.restricted;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.enums.Status;
import ma.apostorial.tmdl_backend.submission.services.interfaces.PlatformerSubmissionService;

@RestController @RequiredArgsConstructor @RequestMapping("/api/staff/platformer-submissions")
public class StaffPlatformerSubmissionController {
    private final PlatformerSubmissionService platformerSubmissionService;
    
    @GetMapping
    public ResponseEntity<Page<PlatformerSubmissionResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(platformerSubmissionService.query(query, status, page, size), HttpStatus.OK);
    }

    @PatchMapping("/status/{submissionId}")
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID submissionId,
            @RequestParam Status status,
            @AuthenticationPrincipal Jwt jwt) {
        platformerSubmissionService.changeStatus(submissionId, status, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
