package ma.apostorial.tmdl_backend.changelog.controllers.restricted;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogCreationRequest;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogResponse;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogUpdateRequest;
import ma.apostorial.tmdl_backend.changelog.services.interfaces.ChangelogService;

@RestController @RequiredArgsConstructor @RequestMapping("/api/staff/changelogs")
public class StaffChangelogController {
    private final ChangelogService changelogService;
    
    @PostMapping("/create")
    public ResponseEntity<ChangelogResponse> create(
            @Valid @RequestBody ChangelogCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(changelogService.create(request, jwt), HttpStatus.CREATED);
    }

    @PatchMapping("update/{changelogId}")
    public ResponseEntity<ChangelogResponse> update(
            @PathVariable UUID changelogId,
            @Valid @RequestBody ChangelogUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(changelogService.update(changelogId, request, jwt), HttpStatus.OK);    
    }

    @DeleteMapping("/delete/{changelogId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID changelogId,
            @AuthenticationPrincipal Jwt jwt) {
        changelogService.deleteById(changelogId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{changelogId}/pin-toggle")
    public ResponseEntity<Void> toggleChangelogPin(
        @PathVariable UUID changelogId,
        @AuthenticationPrincipal Jwt jwt) {
        changelogService.togglePin(changelogId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
