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
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordUpdateRequest;
import ma.apostorial.tmdl_backend.record.services.interfaces.ClassicRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/classic-records")
public class StaffClassicRecordController {
    private final ClassicRecordService classicRecordService;

    @GetMapping("/{recordId}")
    public ResponseEntity<ClassicRecordResponse> findById(@PathVariable UUID recordId) {
        return new ResponseEntity<>(classicRecordService.findById(recordId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ClassicRecordResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(classicRecordService.query(query, page, size), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ClassicRecordResponse> create(
            @Valid @RequestBody ClassicRecordCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicRecordService.create(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/update/{recordId}")
    public ResponseEntity<ClassicRecordResponse> update(
            @PathVariable UUID recordId,
            @Valid @RequestBody ClassicRecordUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(classicRecordService.update(recordId, request, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable UUID recordId,
            @AuthenticationPrincipal Jwt jwt) {
        classicRecordService.deleteById(recordId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
