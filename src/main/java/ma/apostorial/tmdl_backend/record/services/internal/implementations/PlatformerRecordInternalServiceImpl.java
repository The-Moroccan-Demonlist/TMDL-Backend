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
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.record.repositories.PlatformerRecordRepository;
import ma.apostorial.tmdl_backend.record.services.internal.interfaces.PlatformerRecordInternalService;

@Service @Transactional @RequiredArgsConstructor
public class PlatformerRecordInternalServiceImpl implements PlatformerRecordInternalService {
    private final PlatformerRecordRepository platformerRecordRepository;
    
    @Override
    public PlatformerRecord findById(UUID recordId) {
        return platformerRecordRepository.findById(recordId).orElseThrow(() -> new EntityNotFoundException("Platformer record with id " + recordId + " not found."));
    }

    @Override
    public Page<PlatformerRecord> query(String query, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<PlatformerRecord> result = platformerRecordRepository.query(query, pageable);

        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }

        return result;
    }
}
