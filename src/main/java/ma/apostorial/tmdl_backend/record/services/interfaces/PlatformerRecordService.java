package ma.apostorial.tmdl_backend.record.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordUpdateRequest;

public interface PlatformerRecordService {
    PlatformerRecordResponse create(PlatformerRecordCreationRequest request, Jwt jwt);
    PlatformerRecordResponse findById(UUID recordId);
    Page<PlatformerRecordResponse> query(String query, int page, int size);
    PlatformerRecordResponse update(UUID recordId, PlatformerRecordUpdateRequest request, Jwt jwt);
    void deleteById(UUID recordId, Jwt jwt);
}
