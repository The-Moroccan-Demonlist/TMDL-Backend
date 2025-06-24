package ma.apostorial.tmdl_backend.player.controllers.restricted;

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
import ma.apostorial.tmdl_backend.player.dtos.PlayerQueryResponse;
import ma.apostorial.tmdl_backend.player.services.interfaces.PlayerService;

@RestController @RequiredArgsConstructor @RequestMapping("/api/staff/players")
public class StaffPlayerController {
    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity<Page<PlayerQueryResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(playerService.query(query, page, size), HttpStatus.OK);
    }

    @PatchMapping("/{playerId}/flag-toggle")
    public ResponseEntity<Void> togglePlayerFlag(@PathVariable UUID playerId, @AuthenticationPrincipal Jwt jwt) {
        playerService.togglePlayerFlag(playerId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{playerId}/add-badge/{badgeId}")
    public ResponseEntity<Void> addBadge(
        @PathVariable UUID playerId,
        @PathVariable UUID badgeId,
        @AuthenticationPrincipal Jwt jwt) {
        playerService.addBadge(playerId, badgeId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{playerId}/remove-badge/{badgeId}")
    public ResponseEntity<Void> removeBadge(
        @PathVariable UUID playerId,
        @PathVariable UUID badgeId,
        @AuthenticationPrincipal Jwt jwt) {
        playerService.removeBadge(playerId, badgeId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
