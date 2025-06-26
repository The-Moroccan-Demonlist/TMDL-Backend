package ma.apostorial.tmdl_backend.level.controllers.restricted;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.services.interfaces.ClassicLevelService;

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
@RequestMapping("/api/staff/classic-levels")
public class StaffClassicLevelController {
    private final ClassicLevelService classicLevelService;

    @PostMapping("/create")
    public ResponseEntity<ClassicLevelResponse> create(
            @Valid @RequestBody ClassicLevelCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicLevelService.create(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/{levelId}/move/{ranking}")
    public ResponseEntity<Void> move(
            @PathVariable UUID levelId,
            @PathVariable int ranking,
            @AuthenticationPrincipal Jwt jwt) {
        classicLevelService.move(levelId, ranking, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{firstLevelId}/swap/{secondLevelId}")
    public ResponseEntity<Void> swap(
            @PathVariable UUID firstLevelId,
            @PathVariable UUID secondLevelId,
            @AuthenticationPrincipal Jwt jwt) {
        classicLevelService.swap(firstLevelId, secondLevelId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/update/{levelId}")
    public ResponseEntity<ClassicLevelResponse> update(
            @PathVariable UUID levelId, @Valid @RequestBody ClassicLevelUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicLevelService.update(levelId, request, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{levelId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable UUID levelId,
            @AuthenticationPrincipal Jwt jwt) {
        classicLevelService.deleteById(levelId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
