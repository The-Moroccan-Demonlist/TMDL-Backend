package ma.apostorial.tmdl_backend.player.utils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    private static final String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateUsername() {
        return "user" + RANDOM.ints(10, 0, ALPHANUMERIC.length())
                .mapToObj(ALPHANUMERIC::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}