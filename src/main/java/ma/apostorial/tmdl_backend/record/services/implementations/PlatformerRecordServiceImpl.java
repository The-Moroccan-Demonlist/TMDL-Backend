package ma.apostorial.tmdl_backend.record.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.PlatformerLevelInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordUpdateRequest;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.record.mappers.PlatformerRecordMapper;
import ma.apostorial.tmdl_backend.record.repositories.PlatformerRecordRepository;
import ma.apostorial.tmdl_backend.record.services.interfaces.PlatformerRecordService;
import ma.apostorial.tmdl_backend.record.services.internal.interfaces.PlatformerRecordInternalService;

@Service
@Transactional
@RequiredArgsConstructor
public class PlatformerRecordServiceImpl implements PlatformerRecordService {
    private final PlatformerRecordRepository platformerRecordRepository;
    private final PlatformerRecordInternalService platformerRecordInternalService;
    private final PlayerInternalService playerInternalService;
    private final PlatformerRecordMapper platformerRecordMapper;
    private final PlatformerLevelInternalService platformerLevelInternalService;
    private final LogInternalService logInternalService;

    @Override
    public PlatformerRecordResponse create(PlatformerRecordCreationRequest request, Jwt jwt) {
        PlatformerLevel level = platformerLevelInternalService.findById(request.level());
        Player player = playerInternalService.findById(request.player());

        if (level.getRecordHolder() == null ||
                level.getRecordHolder().getPlatformerRecords().stream()
                        .filter(r -> r.getLevel().equals(level))
                        .findFirst()
                        .get()
                        .getRecordTime().compareTo(request.recordTime()) > 0) {
            level.setRecordHolder(player);
        }

        PlatformerRecord record = platformerRecordMapper.fromCreationRequestToEntity(request);
        record.setLevel(level);
        record.setPlayer(player);
        PlatformerRecord savedRecord = platformerRecordRepository.save(record);
        playerInternalService.addPlatformerPoints(savedRecord);
        logInternalService.create(Type.PLATFORMER_RECORD, jwt,
                "Created platformer record [id={}] for player [username={}, id={}] and level [name={}, id={}].",
                savedRecord.getId(), player.getUsername(), player.getId(), level.getName(), level.getId());
        return platformerRecordMapper.fromEntityToResponse(savedRecord);
    }

    @Override
    public PlatformerRecordResponse findById(UUID recordId) {
        PlatformerRecord record = platformerRecordInternalService.findById(recordId);
        return platformerRecordMapper.fromEntityToResponse(record);
    }

    @Override
    public Page<PlatformerRecordResponse> query(String query, int page, int size) {
        Page<PlatformerRecord> records = platformerRecordInternalService.query(query, page, size);
        return records.map(platformerRecordMapper::fromEntityToResponse);
    }

    @Override
    public PlatformerRecordResponse update(UUID recordId, PlatformerRecordUpdateRequest request, Jwt jwt) {
        PlatformerRecord originalRecord = platformerRecordInternalService.findById(recordId);
        PlatformerRecord record = platformerRecordMapper.updateFromRequest(request, originalRecord);
        logInternalService.create(Type.PLATFORMER_RECORD, jwt,
                "Updated platformer record [id={}] for player [username={}, id={}] and level [name={}, id={}].",
                recordId, record.getPlayer().getUsername(), record.getPlayer().getId(), record.getLevel().getName(),
                record.getLevel().getId());
        return platformerRecordMapper.fromEntityToResponse(record);
    }

    @Override
    public void deleteById(UUID recordId, Jwt jwt) {
        PlatformerRecord record = platformerRecordInternalService.findById(recordId);
        PlatformerLevel level = record.getLevel();
        if (level.getRecordHolder() != null && level.getRecordHolder().getId().equals(record.getPlayer().getId())) {
            level.setRecordHolder(null);
        }
        playerInternalService.removePlatformerPoints(record);
        platformerRecordRepository.deleteById(recordId);
        logInternalService.create(Type.PLATFORMER_RECORD, jwt,
                "Deleted platformer record [id={}] for player [username={}, id={}] and level [name={}, id={}].",
                recordId, record.getPlayer().getUsername(), record.getPlayer().getId(), record.getLevel().getName(),
                record.getLevel().getId());
    }

}
