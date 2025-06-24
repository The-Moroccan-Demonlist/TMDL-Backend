package ma.apostorial.tmdl_backend.submission.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.submission.entities.ClassicSubmission;
import ma.apostorial.tmdl_backend.submission.enums.Status;

@Repository
public interface ClassicSubmissionRepository extends JpaRepository<ClassicSubmission, UUID> {
    @Query("""
        SELECT submission FROM ClassicSubmission submission
        WHERE
            (COALESCE(:query, '') = '' OR
                LOWER(submission.player.username) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(submission.level.name) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:status IS NULL OR submission.status = :status)
        ORDER BY submission.submissionDate DESC
    """)
    Page<ClassicSubmission> query(
        @Param("query") String query,
        @Param("status") Status status,
        Pageable pageable
    );

    Page<ClassicSubmission> findByPlayerIdOrderBySubmissionDateDesc(UUID playerId, Pageable pageable);
}
