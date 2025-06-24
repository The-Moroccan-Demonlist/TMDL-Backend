package ma.apostorial.tmdl_backend.level.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;

@Repository
public interface PlatformerLevelRepository extends JpaRepository<PlatformerLevel, UUID> {

    @EntityGraph(attributePaths = {"records", "records.player", "records.player.region"})
    List<PlatformerLevel> findAllByIdIn(List<UUID> levelIds);

    @Query("""
        SELECT level FROM PlatformerLevel level
        WHERE
            (COALESCE(:query, '') = '' OR
                LOWER(level.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(level.publisher) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:difficulty IS NULL OR level.difficulty = :difficulty)
            AND (
                (:type = 'main' AND level.ranking BETWEEN 1 AND 75) OR
                (:type = 'extended' AND level.ranking BETWEEN 76 AND 150) OR
                (:type = 'legacy' AND level.ranking >= 151) OR
                (:type IS NULL OR :type = '' )
            )
        ORDER BY level.ranking ASC
    """)
    List<PlatformerLevel> query(
        @Param("query") String query,
        @Param("difficulty") Difficulty difficulty,
        @Param("type") String type
    );

    Optional<PlatformerLevel> findByIngameId(String ingameId);
    List<PlatformerLevel> findByRankingGreaterThanEqualOrderByRanking(int ranking);
    List<PlatformerLevel> findByRankingGreaterThanEqualAndRankingLessThanOrderByRanking(int newRanking, int oldRanking);
    List<PlatformerLevel> findByRankingGreaterThanAndRankingLessThanEqualOrderByRanking(int newRanking, int oldRanking);
}
