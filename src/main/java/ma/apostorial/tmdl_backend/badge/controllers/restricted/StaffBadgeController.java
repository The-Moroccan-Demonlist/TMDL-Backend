package ma.apostorial.tmdl_backend.badge.controllers.restricted;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeCreationRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeReorderRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeResponse;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeUpdateRequest;
import ma.apostorial.tmdl_backend.badge.services.interfaces.BadgeService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/badges")
public class StaffBadgeController {
    private final BadgeService badgeService;

    @PostMapping("/create")
    public ResponseEntity<BadgeResponse> create(
            @Valid @RequestBody BadgeCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(badgeService.create(request, jwt), HttpStatus.OK);
    }

    @GetMapping("/{badgeId}")
    public ResponseEntity<BadgeResponse> findById(@PathVariable UUID badgeId) {
        return new ResponseEntity<>(badgeService.findById(badgeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BadgeResponse>> findAll() {
        return new ResponseEntity<>(badgeService.findAll(), HttpStatus.OK);
    }

    @PatchMapping("/update/{badgeId}")
    public ResponseEntity<BadgeResponse> update(
            @PathVariable UUID badgeId,
            @Valid @RequestBody BadgeUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(badgeService.update(badgeId, request, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{badgeId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable UUID badgeId,
            @AuthenticationPrincipal Jwt jwt) {
        badgeService.deleteById(badgeId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/reorder")
    public ResponseEntity<Void> reorder(
            @Valid @RequestBody List<BadgeReorderRequest> badges,
            @AuthenticationPrincipal Jwt jwt) {
        badgeService.reorder(badges, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
