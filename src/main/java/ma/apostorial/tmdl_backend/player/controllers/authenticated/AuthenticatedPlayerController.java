package ma.apostorial.tmdl_backend.player.controllers.authenticated;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.player.dtos.PlayerProfileResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerShortResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerUpdateRequest;
import ma.apostorial.tmdl_backend.player.services.interfaces.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authenticated/players")
public class AuthenticatedPlayerController {
    private final PlayerService playerService;

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {
        if (file.getSize() > 5 * 1024 * 1024) {
            return new ResponseEntity<>("File too large", HttpStatus.PAYLOAD_TOO_LARGE);
        }
        return new ResponseEntity<>(playerService.uploadAvatar(file, jwt), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<PlayerShortResponse> getAuthenticatedPlayer(
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(playerService.getAuthenticatedPlayer(jwt), HttpStatus.OK);
    }

    @PatchMapping("/profile/update")
    public ResponseEntity<PlayerProfileResponse> updateAuthenticatedPlayer(
            @Valid @RequestBody PlayerUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(playerService.updateAuthenticatedPlayer(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/profile/region/{regionId}")
    public ResponseEntity<Void> addRegion(
            @PathVariable UUID regionId,
            @AuthenticationPrincipal Jwt jwt) {
        playerService.addRegion(regionId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
