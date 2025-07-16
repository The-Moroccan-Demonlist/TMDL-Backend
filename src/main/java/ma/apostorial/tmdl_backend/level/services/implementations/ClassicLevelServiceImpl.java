package ma.apostorial.tmdl_backend.level.services.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ma.apostorial.tmdl_backend.region.entities.Region;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.apostorial.tmdl_backend.common.exceptions.IllegalArgumentException;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.level.mappers.ClassicLevelMapper;
import ma.apostorial.tmdl_backend.level.repositories.ClassicLevelRepository;
import ma.apostorial.tmdl_backend.level.services.interfaces.ClassicLevelService;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.ClassicLevelInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.services.interfaces.ClassicRecordService;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class ClassicLevelServiceImpl implements ClassicLevelService {
    private final PlayerInternalService playerInternalService;
    private final ClassicRecordService classicRecordService;
    private final ClassicLevelInternalService classicLevelInternalService;
    private final ClassicLevelRepository classicLevelRepository;
    private final ClassicLevelMapper classicLevelMapper;
    private final LogInternalService logInternalService;
    private final S3Client s3Client;
    
    @Value("${MINIO_LEVEL_BUCKET}")
    private String bucket;

    @Value("${MINIO_DOMAIN}")
    private String domain;

    @Override
    public ClassicLevelResponse create(ClassicLevelCreationRequest request, MultipartFile file, Jwt jwt) {
        ClassicLevel levelToCreate = classicLevelMapper.fromCreationRequestToEntity(request);
        levelToCreate.calculatePoints();
        String thumbnailLink = uploadThumbnail(file, request.ingameId());
        levelToCreate.setThumbnailLink(thumbnailLink);
        ClassicLevel savedLevel = classicLevelRepository.save(levelToCreate);

        List<ClassicLevel> affectedLevels = classicLevelRepository.findByRankingGreaterThanEqualOrderByRanking(savedLevel.getRanking());
        List<ClassicRecord> recordsToCheck = new ArrayList<>();
        if (affectedLevels.size() <= 1) {
            logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Created classic level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
            return classicLevelMapper.fromEntityToResponse(savedLevel);
        }
        for (ClassicLevel level : affectedLevels) {
            if (!level.getId().equals(savedLevel.getId())) {
                level.setRanking(level.getRanking() + 1);
            }
            if (level.getRanking() > 75) {
                level.setMinimumCompletion(100);
            }
            if (level.getRanking() > 150) {
                level.setOldPoints(level.getPoints());
                level.setOldMinimumPoints(level.getMinimumPoints());
                level.setPoints(0.0);
                level.setMinimumPoints(0.0);
                level.calculatePoints();
                recordsToCheck.addAll(level.getRecords());
                continue;
            }
            if (level.getId().equals(savedLevel.getId())) {
                continue;
            }
            level.setOldPoints(level.getPoints());
            level.setOldMinimumPoints(level.getMinimumPoints());
            level.calculatePoints();
            recordsToCheck.addAll(level.getRecords());
        }
        if (recordsToCheck.isEmpty()) {
            logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Created classic level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
            return classicLevelMapper.fromEntityToResponse(savedLevel);
        }

        for (ClassicRecord record : recordsToCheck) {
            ClassicLevel level = record.getLevel();
            Player player = record.getPlayer();
            Region region = player.getRegion();

            Double oldValue = (record.getRecordPercentage() == 100) ? level.getOldPoints() : level.getOldMinimumPoints();
            Double newValue = (record.getRecordPercentage() == 100) ? level.getPoints() : level.getMinimumPoints();

            Double oldPlayerPoints = player.getClassicPoints();
            Double newPlayerPoints = oldPlayerPoints - oldValue + newValue;

            Double oldRegionPoints = region.getClassicPoints();
            Double newRegionPoints = oldRegionPoints - oldValue + newValue;

            if (Double.compare(oldPlayerPoints, newPlayerPoints) != 0) {
                player.setClassicPoints(newPlayerPoints);
            }

            if (Double.compare(oldRegionPoints, newRegionPoints) != 0) {
                region.setClassicPoints(newRegionPoints);
            }
        }

        classicLevelRepository.saveAll(affectedLevels);
        logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Created classic level [name={}, id={}].", savedLevel.getName(), savedLevel.getId());
        return classicLevelMapper.fromEntityToResponse(savedLevel);
    }

    @Override
    public void move(UUID levelId, int newRanking, Jwt jwt) {
        long start = System.currentTimeMillis();
        ClassicLevel levelToMove = classicLevelInternalService.findById(levelId);
        int originalRanking = levelToMove.getRanking();
        List<ClassicLevel> affectedLevels = new ArrayList<>();
        List<ClassicRecord> recordsToCheck = new ArrayList<>();
        levelToMove.setRanking(newRanking);
        if (newRanking < originalRanking) {
            affectedLevels = classicLevelRepository.findByRankingGreaterThanEqualAndRankingLessThanOrderByRanking(newRanking, originalRanking);
            for (ClassicLevel level : affectedLevels) {
                if (!level.getId().equals(levelToMove.getId())) {
                    level.setRanking(level.getRanking() + 1);
                }
                if (level.getRanking() > 75) {
                    level.setMinimumCompletion(100);
                }
                if (level.getRanking() > 150) {
                    level.setOldPoints(level.getPoints());
                    level.setOldMinimumPoints(level.getMinimumPoints());
                    level.setPoints(0.0);
                    level.setMinimumPoints(0.0);
                    level.calculatePoints();
                    recordsToCheck.addAll(level.getRecords());
                    continue;
                }
                level.setOldPoints(level.getPoints());
                level.setOldMinimumPoints(level.getMinimumPoints());
                level.calculatePoints();
                recordsToCheck.addAll(level.getRecords());
            }
        } else if (newRanking > originalRanking) {
            affectedLevels = classicLevelRepository.findByRankingGreaterThanAndRankingLessThanEqualOrderByRanking(originalRanking, newRanking);
            for (ClassicLevel level : affectedLevels) {
                if (!level.getId().equals(levelToMove.getId())) {
                    level.setRanking(level.getRanking() - 1);
                }
                if (level.getRanking() > 75) {
                    level.setMinimumCompletion(100);
                }
                if (level.getRanking() > 150) {
                    level.setOldPoints(level.getPoints());
                    level.setOldMinimumPoints(level.getMinimumPoints());
                    level.setPoints(0.0);
                    level.setMinimumPoints(0.0);
                    level.calculatePoints();
                    recordsToCheck.addAll(level.getRecords());
                    continue;
                }
                level.setOldPoints(level.getPoints());
                level.setOldMinimumPoints(level.getMinimumPoints());
                level.calculatePoints();
                recordsToCheck.addAll(level.getRecords());
            }
        } else {
            throw new IllegalArgumentException("Cannot move a level to a spot that it's already in.");
        }

        if (recordsToCheck.isEmpty()) {
            logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Moved classic level [name={}, id={}] from #{} to #{} in {}ms.", levelToMove.getName(), levelToMove.getId(), originalRanking, newRanking, System.currentTimeMillis() - start);
            return;
        }

        for (ClassicRecord record : recordsToCheck) {
            ClassicLevel level = record.getLevel();
            Player player = record.getPlayer();
            Region region = player.getRegion();

            Double oldValue = (record.getRecordPercentage() == 100) ? level.getOldPoints() : level.getOldMinimumPoints();
            Double newValue = (record.getRecordPercentage() == 100) ? level.getPoints() : level.getMinimumPoints();

            Double oldPlayerPoints = player.getClassicPoints();
            Double newPlayerPoints = oldPlayerPoints - oldValue + newValue;

            Double oldRegionPoints = region.getClassicPoints();
            Double newRegionPoints = oldRegionPoints - oldValue + newValue;

            if (Double.compare(oldPlayerPoints, newPlayerPoints) != 0) {
                player.setClassicPoints(newPlayerPoints);
            }

            if (Double.compare(oldRegionPoints, newRegionPoints) != 0) {
                region.setClassicPoints(newRegionPoints);
            }
        }

        classicLevelRepository.saveAll(affectedLevels);
        logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Moved classic level [name={}, id={}] from #{} to #{} and updated affected players and regions in {}ms.", levelToMove.getName(), levelToMove.getId(), originalRanking, newRanking, System.currentTimeMillis() - start);
    }

    @Override
    public void swap(UUID firstLevelId, UUID secondLevelId, Jwt jwt) {
        ClassicLevel firstLevel = classicLevelInternalService.findById(firstLevelId);
        ClassicLevel secondLevel = classicLevelInternalService.findById(secondLevelId);

        List<ClassicLevel> levelsToSave = new ArrayList<>();
        List<ClassicRecord> recordsToCheck = new ArrayList<>();

        int tempRanking = firstLevel.getRanking();
        firstLevel.setRanking(secondLevel.getRanking());
        secondLevel.setRanking(tempRanking);

        firstLevel.setOldPoints(firstLevel.getPoints());
        firstLevel.setOldMinimumPoints(firstLevel.getMinimumPoints());
        
        secondLevel.setOldPoints(secondLevel.getPoints());
        secondLevel.setOldMinimumPoints(secondLevel.getMinimumPoints());

        firstLevel.calculatePoints();
        secondLevel.calculatePoints();

        levelsToSave.add(firstLevel);
        levelsToSave.add(secondLevel);

        recordsToCheck.addAll(firstLevel.getRecords());
        recordsToCheck.addAll(secondLevel.getRecords());

        for (ClassicRecord record : recordsToCheck) {
            ClassicLevel level = record.getLevel();
            Player player = record.getPlayer();
            Region region = player.getRegion();

            Double oldValue = (record.getRecordPercentage() == 100) ? level.getOldPoints() : level.getOldMinimumPoints();
            Double newValue = (record.getRecordPercentage() == 100) ? level.getPoints() : level.getMinimumPoints();

            Double oldPlayerPoints = player.getClassicPoints();
            Double newPlayerPoints = oldPlayerPoints - oldValue + newValue;

            Double oldRegionPoints = region.getClassicPoints();
            Double newRegionPoints = oldRegionPoints - oldValue + newValue;

            if (Double.compare(oldPlayerPoints, newPlayerPoints) != 0) {
                player.setClassicPoints(newPlayerPoints);
            }

            if (Double.compare(oldRegionPoints, newRegionPoints) != 0) {
                region.setClassicPoints(newRegionPoints);
            }
        }

        classicLevelRepository.saveAll(levelsToSave);
        logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Swapped classic level [ranking={}, name={}, id={}] with classic level [ranking={}, name={}, id={}].", secondLevel.getRanking(), firstLevel.getName(), firstLevel.getId(), firstLevel.getRanking(), secondLevel.getName(), secondLevel.getId());
    }

    @Override
    public ClassicLevelResponse findById(UUID levelId) {
        ClassicLevel level = classicLevelInternalService.findById(levelId);
        return classicLevelMapper.fromEntityToResponse(level);
    }

    @Override
    public ClassicLevelResponse findByIngameId(String ingameId) {
        ClassicLevel level = classicLevelInternalService.findByIngameId(ingameId);
        return classicLevelMapper.fromEntityToResponse(level);
    }

    @Override
    public ClassicLevelResponse update(UUID levelId, ClassicLevelUpdateRequest request, Jwt jwt) {
        ClassicLevel originalLevel = classicLevelInternalService.findById(levelId);
        String originalName = originalLevel.getName();
        Player newFirstVictor = playerInternalService.findById(request.firstVictor());
        ClassicLevel level = classicLevelMapper.updateFromRequest(request, originalLevel);

        boolean hasRecord = newFirstVictor.getClassicRecords().stream()
        .anyMatch(record -> record.getLevel().getId().equals(level.getId()));

         if (!hasRecord) {
        ClassicRecordCreationRequest recordRequest = new ClassicRecordCreationRequest(
            newFirstVictor.getId(),
            level.getId(),
            100,
            null
        );
        classicRecordService.create(recordRequest, jwt);
    }

        logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Updated classic level [name={}, id={}].", originalName, levelId);
        return classicLevelMapper.fromEntityToResponse(level);
    }

    @Override
    public List<ClassicLevelQueryResponse> query(String query, Difficulty difficulty, Duration duration, String type) {
        List<ClassicLevel> levels = classicLevelInternalService.query(query, difficulty, duration, type);
        return levels.stream()
            .map(classicLevelMapper::fromEntityToQueryResponse)
            .toList();
    }

    @Override
    public void deleteById(UUID levelId, Jwt jwt) {
        ClassicLevel levelToDelete = classicLevelInternalService.findById(levelId);

        List<ClassicLevel> affectedLevels = classicLevelRepository.findByRankingGreaterThanEqualOrderByRanking(levelToDelete.getRanking());
        List<ClassicRecord> recordsToCheck = new ArrayList<>();
        if (affectedLevels.size() <= 1) {
            logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Deleted classic level [name={}, id={}].", levelToDelete.getName(), levelToDelete.getId());
            classicLevelRepository.deleteById(levelId);
            return;
        }
        for (ClassicLevel level : affectedLevels) {
            if (level.getId().equals(levelToDelete.getId())) {
                level.setOldPoints(level.getPoints());
                level.setOldMinimumPoints(level.getMinimumPoints());
                level.setPoints(0.0);
                level.setMinimumPoints(0.0);
                recordsToCheck.addAll(level.getRecords());
                continue;
            }
            level.setRanking(level.getRanking() - 1);
            level.setOldPoints(level.getPoints());
            level.setOldMinimumPoints(level.getMinimumPoints());
            level.calculatePoints();
            recordsToCheck.addAll(level.getRecords());
        }
        if (recordsToCheck.isEmpty()) {
            logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Deleted classic level [name={}, id={}].", levelToDelete.getName(), levelToDelete.getId());
            classicLevelRepository.deleteById(levelId);
            return;
        }

        for (ClassicRecord record : recordsToCheck) {
            ClassicLevel level = record.getLevel();
            Player player = record.getPlayer();
            Region region = player.getRegion();

            Double oldValue = (record.getRecordPercentage() == 100) ? level.getOldPoints() : level.getOldMinimumPoints();
            Double newValue = (record.getRecordPercentage() == 100) ? level.getPoints() : level.getMinimumPoints();

            Double oldPlayerPoints = player.getClassicPoints();
            Double newPlayerPoints = oldPlayerPoints - oldValue + newValue;

            Double oldRegionPoints = region.getClassicPoints();
            Double newRegionPoints = oldRegionPoints - oldValue + newValue;

            if (Double.compare(oldPlayerPoints, newPlayerPoints) != 0) {
                player.setClassicPoints(newPlayerPoints);
            }

            if (Double.compare(oldRegionPoints, newRegionPoints) != 0) {
                region.setClassicPoints(newRegionPoints);
            }
        }

        classicLevelRepository.deleteById(levelId);
        affectedLevels.remove(levelToDelete);
        classicLevelRepository.saveAll(affectedLevels);
        logInternalService.create(Type.CLASSIC_LEVEL, jwt, "Deleted classic level [name={}, id={}].", levelToDelete.getName(), levelToDelete.getId());
    }

    @Override
    public String uploadThumbnail(MultipartFile file, String levelIngameId) {
        try {
            String fileId = levelIngameId.toString();

            Map<String, String> metadata = new HashMap<>();
            metadata.put("original-filename", file.getOriginalFilename());
            metadata.put("level", levelIngameId);

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileId)
                    .contentType(file.getContentType())
                    .metadata(metadata)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
            
            return String.format(domain + "/%s/%s", bucket, fileId);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }
}
