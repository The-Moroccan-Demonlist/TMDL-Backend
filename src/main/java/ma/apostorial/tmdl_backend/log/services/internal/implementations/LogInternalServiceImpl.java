package ma.apostorial.tmdl_backend.log.services.internal.implementations;

import java.util.regex.Matcher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.exceptions.NoContentException;
import ma.apostorial.tmdl_backend.log.entities.Log;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.repositories.LogRepository;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;

@Service @Transactional @RequiredArgsConstructor
public class LogInternalServiceImpl implements LogInternalService {
    private final LogRepository logRepository;
    private final PlayerInternalService playerInternalService;

    @Override
    public Log create(Type type, Jwt jwt, String content, Object... args) {
        String formatted = formatMessage(content, args);
        Player author = playerInternalService.getAuthenticatedPlayer(jwt);
        Log log = Log.builder()
            .author(author)
            .content(formatted)
            .type(type)
            .build();

        return logRepository.save(log);
    }

    private String formatMessage(String content, Object... args) {
        for (Object arg : args) {
            content = content.replaceFirst("\\{\\}", Matcher.quoteReplacement(String.valueOf(arg)));
        }
        return content;
    }



    @Override
    public Page<Log> query(String query, Type type, int page, int size) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Log> result = logRepository.query(query, type, pageable);
        
        if (result.isEmpty()) {
            throw new NoContentException("No results would match these criteria.");
        }
        
        return result;
    }
}
