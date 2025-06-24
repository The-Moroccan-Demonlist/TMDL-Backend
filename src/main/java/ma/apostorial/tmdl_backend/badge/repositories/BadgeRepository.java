package ma.apostorial.tmdl_backend.badge.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.badge.entities.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, UUID> {
    
}
