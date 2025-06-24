package ma.apostorial.tmdl_backend.level.services.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;

public interface ClassicLevelService {
    ClassicLevelResponse create(ClassicLevelCreationRequest request, Jwt jwt);
    void move(UUID levelId, int ranking, Jwt jwt);
    void swap(UUID firstLevelId, UUID secondLevelId, Jwt jwt);
    ClassicLevelResponse findById(UUID levelId);
    ClassicLevelResponse findByIngameId(String ingameId);
    ClassicLevelResponse update(UUID levelId, ClassicLevelUpdateRequest request, Jwt jwt);
    List<ClassicLevelQueryResponse> query(String query, Difficulty difficulty, Duration duration, String type);
    void deleteById(UUID levelId, Jwt jwt);
}
