package ma.apostorial.tmdl_backend.log.services.interfaces;

import org.springframework.data.domain.Page;

import ma.apostorial.tmdl_backend.log.dtos.LogResponse;
import ma.apostorial.tmdl_backend.log.enums.Type;

public interface LogService {
    Page<LogResponse> query(String query, Type type, int page, int size);
}
