package ma.apostorial.tmdl_backend.level.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;

@Repository
public interface ClassicLevelRepository extends JpaRepository<ClassicLevel, UUID> {

    @EntityGraph(attributePaths = {"records", "records.player", "records.player.region"})
    List<ClassicLevel> findAllByIdIn(List<UUID> levelIds);

    @Query("""
        SELECT level FROM ClassicLevel level
        WHERE
            (COALESCE(:query, '') = '' OR
                LOWER(level.name) LIKE LOWER(CONCAT('%', :query, '%')) OR
                LOWER(level.publisher) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:difficulty IS NULL OR level.difficulty = :difficulty)
            AND (:duration IS NULL OR level.duration = :duration)
            AND (
                (:type = 'main' AND level.ranking BETWEEN 1 AND 75) OR
                (:type = 'extended' AND level.ranking BETWEEN 76 AND 150) OR
                (:type = 'legacy' AND level.ranking >= 151) OR
                (:type IS NULL OR :type = '' )
            )
        ORDER BY level.ranking ASC
    """)
    List<ClassicLevel> query(
        @Param("query") String query,
        @Param("difficulty") Difficulty difficulty,
        @Param("duration") Duration duration,
        @Param("type") String type
    );

    @Query(value = """
        SELECT * FROM classic_level
        WHERE 
            (:includeMain AND ranking BETWEEN 1 AND 75) OR
            (:includeExtended AND ranking BETWEEN 76 AND 150) OR
            (:includeLegacy AND ranking >= 151)
        ORDER BY random()
        LIMIT 100
    """, nativeQuery = true)
    List<ClassicLevel> rouletteRandomQuery(
        @Param("includeMain") boolean includeMain,
        @Param("includeExtended") boolean includeExtended,
        @Param("includeLegacy") boolean includeLegacy
    );

    Optional<ClassicLevel> findByIngameId(String ingameId);
    List<ClassicLevel> findByRankingGreaterThanEqualOrderByRanking(int ranking);
    List<ClassicLevel> findByRankingGreaterThanEqualAndRankingLessThanOrderByRanking(int newRanking, int oldRanking);
    List<ClassicLevel> findByRankingGreaterThanAndRankingLessThanEqualOrderByRanking(int newRanking, int oldRanking);

}
