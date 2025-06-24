package ma.apostorial.tmdl_backend.region.controllers.restricted;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.region.dtos.RegionCreationRequest;
import ma.apostorial.tmdl_backend.region.dtos.RegionQueryResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionUpdateRequest;
import ma.apostorial.tmdl_backend.region.services.interfaces.RegionService;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/regions")
public class StaffRegionController {
    private final RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<RegionQueryResponse> create(
            @Valid @RequestBody RegionCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(regionService.create(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/update/{regionId}")
    public ResponseEntity<RegionResponse> update(
            @PathVariable UUID regionId,
            @Valid @RequestBody RegionUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(regionService.update(regionId, request, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{regionId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID regionId,
            @AuthenticationPrincipal Jwt jwt) {
        regionService.deleteById(regionId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
