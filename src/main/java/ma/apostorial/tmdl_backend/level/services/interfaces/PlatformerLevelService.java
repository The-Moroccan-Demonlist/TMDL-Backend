package ma.apostorial.tmdl_backend.level.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;

public interface PlatformerLevelService {
    PlatformerLevelResponse create(PlatformerLevelCreationRequest request, Jwt jwt);
    PlatformerLevelResponse findById(UUID levelId);
    PlatformerLevelResponse findByIngameId(String ingameId);
    PlatformerLevelResponse update(UUID levelId, PlatformerLevelUpdateRequest request, Jwt jwt);
    List<PlatformerLevelQueryResponse> query(String query, Difficulty difficulty, String type);
    void deleteById(UUID levelId, Jwt jwt);
}
