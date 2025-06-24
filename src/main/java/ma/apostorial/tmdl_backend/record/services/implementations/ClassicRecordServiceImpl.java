package ma.apostorial.tmdl_backend.record.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.IllegalArgumentException;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.level.services.internal.interfaces.ClassicLevelInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordUpdateRequest;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.mappers.ClassicRecordMapper;
import ma.apostorial.tmdl_backend.record.repositories.ClassicRecordRepository;
import ma.apostorial.tmdl_backend.record.services.interfaces.ClassicRecordService;
import ma.apostorial.tmdl_backend.record.services.internal.interfaces.ClassicRecordInternalService;

@Service @Transactional @RequiredArgsConstructor
public class ClassicRecordServiceImpl implements ClassicRecordService {
    private final ClassicRecordRepository classicRecordRepository;
    private final ClassicRecordInternalService classicRecordInternalService;
    private final PlayerInternalService playerInternalService;
    private final ClassicLevelInternalService classicLevelInternalService;
    private final ClassicRecordMapper classicRecordMapper;
    private final LogInternalService logInternalService;

    @Override
    public ClassicRecordResponse create(ClassicRecordCreationRequest request, Jwt jwt) {
        ClassicLevel level = classicLevelInternalService.findById(request.level());
        Player player = playerInternalService.findById(request.player());

        if (request.recordPercentage() < level.getMinimumCompletion()) {
            throw new IllegalArgumentException("Record percentage cannot be inferior to the level's minimum completion.");
        }

        if (level.getFirstVictor() == null) {
            level.setFirstVictor(player);
        }
        ClassicRecord record = classicRecordMapper.fromCreationRequestToEntity(request);
        record.setLevel(level);
        record.setPlayer(player);
        ClassicRecord savedRecord = classicRecordRepository.save(record);
        playerInternalService.addClassicPoints(savedRecord);
        logInternalService.create(Type.CLASSIC_RECORD, jwt, "Created classic record [id={}] for player [username={}, id={}] and level [name={}, id={}].", savedRecord.getId(), player.getUsername(), player.getId(), level.getName(), level.getId());
        return classicRecordMapper.fromEntityToResponse(savedRecord);
    }

    @Override
    public ClassicRecordResponse findById(UUID recordId) {
        ClassicRecord record = classicRecordInternalService.findById(recordId);
        return classicRecordMapper.fromEntityToResponse(record);
    }

    @Override
    public Page<ClassicRecordResponse> query(String query, int page, int size) {
        Page<ClassicRecord> records = classicRecordInternalService.query(query, page, size);
        return records.map(classicRecordMapper::fromEntityToResponse);
    }

    @Override
    public ClassicRecordResponse update(UUID recordId, ClassicRecordUpdateRequest request, Jwt jwt) {
        ClassicRecord originalRecord = classicRecordInternalService.findById(recordId);
        ClassicLevel level = originalRecord.getLevel();
        if (request.recordPercentage() < level.getMinimumCompletion()) {
            throw new IllegalArgumentException("Record percentage cannot be inferior to the level's minimum completion.");
        }
        ClassicRecord clone = new ClassicRecord();
        clone.setPlayer(originalRecord.getPlayer());
        clone.setLevel(originalRecord.getLevel());
        clone.setRecordPercentage(originalRecord.getRecordPercentage());

        int oldPercentage = clone.getRecordPercentage();
        int newPercentage = request.recordPercentage();

        ClassicRecord record = classicRecordMapper.updateFromRequest(request, originalRecord);

        boolean becameFullClear = oldPercentage < 100 && newPercentage == 100;
        boolean lostFullClear = oldPercentage == 100 && newPercentage < 100;

        if (becameFullClear || lostFullClear) {
        playerInternalService.removeClassicPoints(clone);
        playerInternalService.addClassicPoints(record);
        }
        
        logInternalService.create(Type.CLASSIC_RECORD, jwt, "Updated classic record [id={}] for player [username={}, id={}] and level [name={}, id={}].", recordId, clone.getPlayer().getUsername(), clone.getPlayer().getId(), level.getName(), level.getId());
        return classicRecordMapper.fromEntityToResponse(record);
    }

    @Override
    public void deleteById(UUID recordId, Jwt jwt) {
        ClassicRecord record = classicRecordInternalService.findById(recordId);
        ClassicLevel level = record.getLevel();
        if (level.getFirstVictor() != null && level.getFirstVictor().getId().equals(record.getPlayer().getId())) {
            level.setFirstVictor(null);
        }
        playerInternalService.removeClassicPoints(record);
        classicRecordRepository.deleteById(recordId);
        logInternalService.create(Type.CLASSIC_RECORD, jwt, "Deleted classic record [id={}] for player [username={}, id={}] and level [name={}, id={}].", recordId, record.getPlayer().getUsername(), record.getPlayer().getId(), record.getLevel().getName(), record.getLevel().getId());
    }
}
