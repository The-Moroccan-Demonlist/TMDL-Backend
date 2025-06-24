package ma.apostorial.tmdl_backend.record.services.internal.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;

import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;

public interface ClassicRecordInternalService {
    ClassicRecord findById(UUID recordId);
    Page<ClassicRecord> query(String query, int page, int size);
}
