package ma.apostorial.tmdl_backend.level.controllers.restricted;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.services.interfaces.PlatformerLevelService;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/platformer-levels")
public class StaffPlatformerLevelController {
    private final PlatformerLevelService platformerLevelService;

    @PostMapping("/create")
    public ResponseEntity<PlatformerLevelResponse> create(
            @Valid @RequestBody PlatformerLevelCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerLevelService.create(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/update/{levelId}")
    public ResponseEntity<PlatformerLevelResponse> update(
            @PathVariable UUID levelId,
            @Valid @RequestBody PlatformerLevelUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerLevelService.update(levelId, request, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{levelId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable UUID levelId,
            @AuthenticationPrincipal Jwt jwt) {
        platformerLevelService.deleteById(levelId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
