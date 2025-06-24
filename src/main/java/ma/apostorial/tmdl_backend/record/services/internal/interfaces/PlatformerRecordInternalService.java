package ma.apostorial.tmdl_backend.record.services.internal.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;

import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;

public interface PlatformerRecordInternalService {
    PlatformerRecord findById(UUID recordId);
    Page<PlatformerRecord> query(String query, int page, int size);
}
