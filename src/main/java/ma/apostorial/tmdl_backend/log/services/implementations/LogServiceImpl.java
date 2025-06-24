package ma.apostorial.tmdl_backend.log.services.implementations;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.log.dtos.LogResponse;
import ma.apostorial.tmdl_backend.log.entities.Log;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.mappers.LogMapper;
import ma.apostorial.tmdl_backend.log.services.interfaces.LogService;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;

@Service @Transactional @RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogInternalService logInternalService;
    private final LogMapper logMapper;

    @Override
    public Page<LogResponse> query(String query, Type type, int page, int size) {
        Page<Log> logs = logInternalService.query(query, type, page, size);
        return logs.map(logMapper::fromEntityTResponse);
    }
    
}
