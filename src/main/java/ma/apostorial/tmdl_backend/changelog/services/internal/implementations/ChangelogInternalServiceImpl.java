package ma.apostorial.tmdl_backend.changelog.services.internal.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.changelog.entities.Changelog;
import ma.apostorial.tmdl_backend.changelog.enums.Category;
import ma.apostorial.tmdl_backend.changelog.repositories.ChangelogRepository;
import ma.apostorial.tmdl_backend.changelog.services.internal.interfaces.ChangelogInternalService;
import ma.apostorial.tmdl_backend.common.exceptions.EntityNotFoundException;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;

@Service @Transactional @RequiredArgsConstructor
public class ChangelogInternalServiceImpl implements ChangelogInternalService {
    private final ChangelogRepository changelogRepository;

    @Override
    public Changelog findById(UUID changelogId) {
        return changelogRepository.findById(changelogId).orElseThrow(() -> new EntityNotFoundException("Changelog with id " + changelogId + " not found."));
    }

    @Override
    public Page<Changelog> query(String query, Category category, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Changelog> result = changelogRepository.query(query, category, pageable);
        
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }
}
