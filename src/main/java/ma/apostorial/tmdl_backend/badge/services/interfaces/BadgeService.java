package ma.apostorial.tmdl_backend.badge.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.badge.dtos.BadgeCreationRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeReorderRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeResponse;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeUpdateRequest;

public interface BadgeService {
    BadgeResponse create(BadgeCreationRequest request, Jwt jwt);
    BadgeResponse findById(UUID badgeId);
    List<BadgeResponse> findAll();
    BadgeResponse update(UUID badgeId, BadgeUpdateRequest request, Jwt jwt);
    void deleteById(UUID badgeId, Jwt jwt);  
    void reorder(List<BadgeReorderRequest> badges, Jwt jwt);
}
