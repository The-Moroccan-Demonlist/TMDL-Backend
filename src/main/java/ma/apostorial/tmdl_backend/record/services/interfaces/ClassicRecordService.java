package ma.apostorial.tmdl_backend.record.services.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordUpdateRequest;

public interface ClassicRecordService {
    ClassicRecordResponse create(ClassicRecordCreationRequest request, Jwt jwt);
    ClassicRecordResponse findById(UUID recordId);
    Page<ClassicRecordResponse> query(String query, int page, int size);
    ClassicRecordResponse update(UUID recordId, ClassicRecordUpdateRequest request, Jwt jwt);
    void deleteById(UUID recordId, Jwt jwt);
}
