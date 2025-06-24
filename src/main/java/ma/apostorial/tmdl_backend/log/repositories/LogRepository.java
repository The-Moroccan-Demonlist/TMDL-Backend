package ma.apostorial.tmdl_backend.log.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.log.entities.Log;
import ma.apostorial.tmdl_backend.log.enums.Type;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {
    @Query("""
        SELECT log FROM Log log
        WHERE
            (:query is NULL OR :query = '' OR
                LOWER(log.author.username) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:type IS NULL OR log.type = :type)
        ORDER BY log.date DESC
    """)
    Page<Log> query(
        @Param("query") String query,
        @Param("type") Type type,
        Pageable pageable
    );
}
