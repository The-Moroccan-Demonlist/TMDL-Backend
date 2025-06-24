package ma.apostorial.tmdl_backend.badge.services.internal.interfaces;

import java.util.List;
import java.util.UUID;

import ma.apostorial.tmdl_backend.badge.entities.Badge;

public interface BadgeInternalService {
    Badge findById(UUID badgeId);
    List<Badge> findAll();
    List<Badge> findAllById(List<UUID> badgeIds);
}
