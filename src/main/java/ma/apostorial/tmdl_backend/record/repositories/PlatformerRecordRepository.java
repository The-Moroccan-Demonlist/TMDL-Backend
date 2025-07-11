package ma.apostorial.tmdl_backend.record.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;

@Repository
public interface PlatformerRecordRepository extends JpaRepository<PlatformerRecord, UUID> {
    @Query("""
        SELECT record FROM PlatformerRecord record
        WHERE
            (:query is NULL OR :query = '' OR
                LOWER(record.level.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(record.player.username) LIKE LOWER(CONCAT('%', :query, '%')))
    """)
    Page<PlatformerRecord> query(
        @Param("query") String query,
        Pageable pageable
    );
}
