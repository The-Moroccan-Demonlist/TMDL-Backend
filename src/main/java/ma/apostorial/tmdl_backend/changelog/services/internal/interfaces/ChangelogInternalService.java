package ma.apostorial.tmdl_backend.changelog.services.internal.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;

import ma.apostorial.tmdl_backend.changelog.entities.Changelog;
import ma.apostorial.tmdl_backend.changelog.enums.Category;

public interface ChangelogInternalService {
    Changelog findById(UUID changelogId);
    Page<Changelog> query(String query, Category category, int page, int size);
}
