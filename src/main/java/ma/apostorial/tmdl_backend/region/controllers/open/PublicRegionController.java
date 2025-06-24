package ma.apostorial.tmdl_backend.region.controllers.open;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.region.dtos.RegionQueryResponse;
import ma.apostorial.tmdl_backend.region.services.interfaces.RegionService;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController @RequiredArgsConstructor @RequestMapping("/api/public/regions")
public class PublicRegionController {
    private final RegionService regionService;

    @GetMapping("/{regionId}")
    public ResponseEntity<RegionQueryResponse> findById(@PathVariable UUID regionId) {
        return new ResponseEntity<>(regionService.findById(regionId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<RegionQueryResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(regionService.query(query, page, size), HttpStatus.OK);
    }
}
