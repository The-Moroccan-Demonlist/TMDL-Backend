package ma.apostorial.tmdl_backend.region.services.internal.interfaces;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;

import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.region.entities.Region;

public interface RegionInternalService {
    void saveAll(Set<Region> regions);
    Region findById(UUID regionId);
    Page<Region> query(String query, int page, int size);
    void addClassicPoints(ClassicRecord record);
    void removeClassicPoints(ClassicRecord record);
    void addPlatformerPoints(PlatformerRecord record);
    void removePlatformerPoints(PlatformerRecord record);
}
