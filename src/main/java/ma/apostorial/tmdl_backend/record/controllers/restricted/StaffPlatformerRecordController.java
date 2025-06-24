package ma.apostorial.tmdl_backend.record.controllers.restricted;

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
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordUpdateRequest;
import ma.apostorial.tmdl_backend.record.services.interfaces.PlatformerRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/platformer-records")
public class StaffPlatformerRecordController {
    private final PlatformerRecordService platformerRecordService;

    @GetMapping("/{recordId}")
    public ResponseEntity<PlatformerRecordResponse> findById(@PathVariable UUID recordId) {
        return new ResponseEntity<>(platformerRecordService.findById(recordId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PlatformerRecordResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(platformerRecordService.query(query, page, size), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<PlatformerRecordResponse> create(
            @Valid @RequestBody PlatformerRecordCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerRecordService.create(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/update/{recordId}")
    public ResponseEntity<PlatformerRecordResponse> update(
            @PathVariable UUID recordId,
            @Valid @RequestBody PlatformerRecordUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(platformerRecordService.update(recordId, request, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable UUID recordId,
            @AuthenticationPrincipal Jwt jwt) {
        platformerRecordService.deleteById(recordId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
