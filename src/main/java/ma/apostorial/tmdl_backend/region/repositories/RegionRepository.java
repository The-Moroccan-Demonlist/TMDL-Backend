package ma.apostorial.tmdl_backend.region.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ma.apostorial.tmdl_backend.region.entities.Region;

public interface RegionRepository extends JpaRepository<Region, UUID> {
    Page<Region> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
