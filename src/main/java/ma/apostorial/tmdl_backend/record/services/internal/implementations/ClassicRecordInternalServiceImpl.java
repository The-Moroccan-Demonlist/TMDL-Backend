package ma.apostorial.tmdl_backend.record.services.internal.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.repositories.ClassicRecordRepository;
import ma.apostorial.tmdl_backend.record.services.internal.interfaces.ClassicRecordInternalService;

@Service @Transactional @RequiredArgsConstructor
public class ClassicRecordInternalServiceImpl implements ClassicRecordInternalService {
    private final ClassicRecordRepository classicRecordRepository;
    
    @Override
    public ClassicRecord findById(UUID recordId) {
        return classicRecordRepository.findById(recordId).orElseThrow(() -> new EntityNotFoundException("Classic record with id " + recordId + " not found."));
    }

    @Override
    public Page<ClassicRecord> query(String query, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ClassicRecord> result = classicRecordRepository.query(query, pageable);

        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }

        return result;
    }
}
