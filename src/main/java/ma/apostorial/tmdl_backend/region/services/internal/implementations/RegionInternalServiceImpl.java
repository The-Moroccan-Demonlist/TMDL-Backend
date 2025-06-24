package ma.apostorial.tmdl_backend.region.services.internal.implementations;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.region.entities.Region;
import ma.apostorial.tmdl_backend.region.repositories.RegionRepository;
import ma.apostorial.tmdl_backend.region.services.internal.interfaces.RegionInternalService;

@Service @Transactional @RequiredArgsConstructor
public class RegionInternalServiceImpl implements RegionInternalService {
    private final RegionRepository regionRepository;

    @Override
    public void saveAll(Set<Region> regions) {
        regionRepository.saveAll(regions);
    }    

    @Override
    public Region findById(UUID regionId) {
        return regionRepository.findById(regionId).orElseThrow(() -> new EntityNotFoundException("Region with id " + regionId + " not found."));
    }

    @Override
    public Page<Region> query(String query, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Region> result;
        if (StringUtils.hasText(query)) {
            result = regionRepository.findByNameContainingIgnoreCase(query, pageable);
        } else {
            result = regionRepository.findAll(pageable);
        }
        
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }

    @Override
    public void addClassicPoints(ClassicRecord record) {
        ClassicLevel level = record.getLevel();
        Region region = record.getPlayer().getRegion();
        if (record.getRecordPercentage() == 100) {
            region.setClassicPoints(region.getClassicPoints() + level.getPoints());
        } else {
            region.setClassicPoints(region.getClassicPoints() + level.getMinimumPoints());
        }
        regionRepository.save(region);
    }

    @Override
    public void removeClassicPoints(ClassicRecord record) {
        ClassicLevel level = record.getLevel();
        Region region = record.getPlayer().getRegion();
        if (record.getRecordPercentage() == 100) {
            region.setClassicPoints(region.getClassicPoints() - level.getPoints());
        } else {
            region.setClassicPoints(region.getClassicPoints() - level.getMinimumPoints());
        }
        regionRepository.save(region);
    }

    @Override
    public void addPlatformerPoints(PlatformerRecord record) {
        PlatformerLevel level = record.getLevel();
        Region region = record.getPlayer().getRegion();
        region.setPlatformerPoints(region.getPlatformerPoints() + level.getPoints());
        regionRepository.save(region);
    }

    @Override
    public void removePlatformerPoints(PlatformerRecord record) {
        PlatformerLevel level = record.getLevel();
        Region region = record.getPlayer().getRegion();
        region.setPlatformerPoints(region.getPlatformerPoints() - level.getPoints());
        regionRepository.save(region);
    }
}
