package ma.apostorial.tmdl_backend.region.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.region.dtos.RegionCreationRequest;
import ma.apostorial.tmdl_backend.region.dtos.RegionQueryResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionUpdateRequest;
import ma.apostorial.tmdl_backend.region.entities.Region;
import ma.apostorial.tmdl_backend.region.mappers.RegionMapper;
import ma.apostorial.tmdl_backend.region.repositories.RegionRepository;
import ma.apostorial.tmdl_backend.region.services.interfaces.RegionService;
import ma.apostorial.tmdl_backend.region.services.internal.interfaces.RegionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    private final RegionInternalService regionInternalService;
    private final RegionMapper regionMapper;
    private final LogInternalService logInternalService;

    @Override
    public RegionQueryResponse create(RegionCreationRequest request, Jwt jwt) {
        Region region = regionMapper.fromCreationRequestToEntity(request);
        Region savedRegion = regionRepository.save(region);
        logInternalService.create(Type.REGION, jwt, "Created region [name={}, id={}].", savedRegion.getName(), savedRegion.getId());
        return regionMapper.fromEntityToQueryResponse(savedRegion);
    }

    @Override
    public RegionQueryResponse findById(UUID regionId) {
        Region region = regionInternalService.findById(regionId);
        return regionMapper.fromEntityToQueryResponse(region);
    }

    @Override
    public Page<RegionQueryResponse> query(String query, int page, int size) {
        Page<Region> regions = regionInternalService.query(query, page, size);
        return regions.map(regionMapper::fromEntityToQueryResponse);
    }

    @Override
    public RegionResponse update(UUID regionId, RegionUpdateRequest request, Jwt jwt) {
        Region originalRegion = regionInternalService.findById(regionId);
        String originalName = originalRegion.getName();
        Region region = regionMapper.updateFromRequest(request, originalRegion);
        logInternalService.create(Type.REGION, jwt, "Updated region [name={}, id={}].", originalName, regionId);
        return regionMapper.fromEntityToResponse(region);
    }

    @Override
    public void deleteById(UUID regionId, Jwt jwt) {
        Region region = regionInternalService.findById(regionId);
        regionRepository.deleteById(regionId);
        logInternalService.create(Type.REGION, jwt, "Deleted region [name={}, id={}].", region.getName(), regionId);
    }
}
