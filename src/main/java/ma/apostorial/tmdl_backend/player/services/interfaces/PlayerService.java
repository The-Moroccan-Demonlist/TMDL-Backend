package ma.apostorial.tmdl_backend.player.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import ma.apostorial.tmdl_backend.player.dtos.PlayerPostLoginRequest;
import ma.apostorial.tmdl_backend.player.dtos.PlayerProfileResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerQueryResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerShortResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerUpdateRequest;

public interface PlayerService {
    void createIfNotExists(PlayerPostLoginRequest request);
    PlayerProfileResponse findById(UUID id);
    PlayerProfileResponse findByUsername(String username);
    Page<PlayerQueryResponse> query(String query, int page, int size);
    List<PlayerQueryResponse> findAllByClassicPoints();
    List<PlayerQueryResponse> findAllByPlatformerPoints();
    PlayerShortResponse getAuthenticatedPlayer(Jwt jwt);
    PlayerProfileResponse updateAuthenticatedPlayer(PlayerUpdateRequest request, Jwt jwt);
    // List<PlayerQueryResponse> findAllStaff();
    void addRegion(UUID regionId, Jwt jwt);
    void togglePlayerFlag(UUID playerId, Jwt jwt);
    void addBadge(UUID playerId, UUID badgeId, Jwt jwt);
    void removeBadge(UUID playerId, UUID badgeId, Jwt jwt);
    String uploadAvatar(MultipartFile avatar, Jwt jwt);
}
