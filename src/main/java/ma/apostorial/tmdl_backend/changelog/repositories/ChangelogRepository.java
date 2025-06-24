package ma.apostorial.tmdl_backend.changelog.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.changelog.entities.Changelog;
import ma.apostorial.tmdl_backend.changelog.enums.Category;

@Repository
public interface ChangelogRepository extends JpaRepository<Changelog, UUID> {
    @Query("""
        SELECT changelog FROM Changelog changelog
        WHERE (:query IS NULL OR :query = '' OR LOWER(changelog.content) LIKE LOWER(CONCAT('%', :query, '%')))
        AND (:category IS NULL OR changelog.category = :category)
        ORDER BY changelog.isPinned DESC, changelog.publishedAt DESC
    """)
    Page<Changelog> query(
        @Param("query") String query,
        @Param("category") Category category,
        Pageable pageable
    );
}
