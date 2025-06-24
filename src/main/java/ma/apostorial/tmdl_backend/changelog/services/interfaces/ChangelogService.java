package ma.apostorial.tmdl_backend.changelog.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogCreationRequest;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogResponse;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogUpdateRequest;
import ma.apostorial.tmdl_backend.changelog.enums.Category;

public interface ChangelogService {
    ChangelogResponse create(ChangelogCreationRequest request, Jwt jwt);
    Page<ChangelogResponse> query(String query, Category category, int page, int size);
    ChangelogResponse update(UUID changelogId, ChangelogUpdateRequest request, Jwt jwt);
    void deleteById(UUID changelogId, Jwt jwt);
    void togglePin(UUID changelogId, Jwt jwt);
}
