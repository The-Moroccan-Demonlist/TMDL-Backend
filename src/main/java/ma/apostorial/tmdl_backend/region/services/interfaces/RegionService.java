package ma.apostorial.tmdl_backend.region.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.region.dtos.RegionCreationRequest;
import ma.apostorial.tmdl_backend.region.dtos.RegionQueryResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionUpdateRequest;

public interface RegionService { 
    RegionQueryResponse create(RegionCreationRequest request, Jwt jwt);
    RegionQueryResponse findById(UUID regionId);
    Page<RegionQueryResponse> query(String query, int page, int size);
    RegionResponse update(UUID regionId, RegionUpdateRequest request, Jwt jwt);
    void deleteById(UUID regionId, Jwt jwt);
}
