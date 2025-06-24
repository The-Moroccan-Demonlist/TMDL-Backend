package ma.apostorial.tmdl_backend.log.services.internal.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import ma.apostorial.tmdl_backend.log.entities.Log;
import ma.apostorial.tmdl_backend.log.enums.Type;

public interface LogInternalService {
    Log create(Type type, Jwt jwt, String content, Object... args);
    Page<Log> query(String query, Type type, int page, int size);
}
