package ma.apostorial.tmdl_backend.player.services.implementations;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.badge.entities.Badge;
import ma.apostorial.tmdl_backend.badge.services.internal.interfaces.BadgeInternalService;
import ma.apostorial.tmdl_backend.common.exceptions.IllegalArgumentException;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.dtos.PlayerPostLoginRequest;
import ma.apostorial.tmdl_backend.player.dtos.PlayerProfileResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerQueryResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerShortResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerUpdateRequest;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.mappers.PlayerMapper;
import ma.apostorial.tmdl_backend.player.repositories.PlayerRepository;
import ma.apostorial.tmdl_backend.player.services.interfaces.PlayerService;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;
import ma.apostorial.tmdl_backend.player.utils.UsernameGenerator;
import ma.apostorial.tmdl_backend.region.entities.Region;
import ma.apostorial.tmdl_backend.region.services.internal.interfaces.RegionInternalService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerInternalService playerInternalService;
    private final RegionInternalService regionInternalService;
    private final BadgeInternalService badgeInternalService;
    private final PlayerMapper playerMapper;
    private final UsernameGenerator usernameGenerator;
    private final LogInternalService logInternalService;
    private final S3Client s3Client;

    private final String bucket = "avatars";

    @Override
    public void createIfNotExists(PlayerPostLoginRequest request) {
        if (!playerRepository.existsBySub(request.sub())) {
            Player player = Player.builder()
                    .username(usernameGenerator.generateUsername())
                    .sub(request.sub())
                    .email(request.email())
                    .build();
            playerRepository.save(player);
        }
    }

    @Override
    public PlayerProfileResponse findById(UUID playerId) {
        Player player = playerInternalService.findById(playerId);
        return playerMapper.fromEntityToProfileResponse(player);
    }

    @Override
    public PlayerProfileResponse findByUsername(String username) {
        Player player = playerInternalService.findByUsername(username);
        return playerMapper.fromEntityToProfileResponse(player);
    }

    @Override
    public Page<PlayerQueryResponse> query(String query, int page, int size) {
        Page<Player> players = playerInternalService.query(query, page, size);
        return players.map(playerMapper::fromEntityToQueryResponse);
    }

    @Override
    public List<PlayerQueryResponse> findAllByClassicPoints() {
        List<Player> players = playerInternalService.findAllByClassicPoints();
        return players.stream()
                .map(playerMapper::fromEntityToQueryResponse)
                .toList();
    }

    @Override
    public List<PlayerQueryResponse> findAllByPlatformerPoints() {
        List<Player> players = playerInternalService.findAllByPlatformerPoints();
        return players.stream()
                .map(playerMapper::fromEntityToQueryResponse)
                .toList();
    }

    @Override
    public PlayerShortResponse getAuthenticatedPlayer(Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        List<String> permissions = jwt.getClaimAsStringList("permissions");
        return playerMapper.fromEntityToShortResponse(player, permissions);
    }

    @Override
    public PlayerProfileResponse updateAuthenticatedPlayer(PlayerUpdateRequest request, Jwt jwt) {
        Player originalPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        Player player = playerMapper.updateFromRequest(request, originalPlayer);
        return playerMapper.fromEntityToProfileResponse(player);
    }

    // @Override
    // public List<PlayerQueryResponse> findAllStaff() {
    //     List<Player> players = playerInternalService.findAllStaff();
    //     return players.stream()
    //             .map(playerMapper::fromEntityToQueryResponse)
    //             .toList();
    // }

    @Override
    public void addRegion(UUID regionId, Jwt jwt) {
        Player player = playerInternalService.getAuthenticatedPlayer(jwt);
        Region region = regionInternalService.findById(regionId);
        if (!player.isActive()) {
            player.setActive(true);
        }
        player.setRegion(region);
        playerRepository.save(player);
    }

    @Override
    public void togglePlayerFlag(UUID playerId, Jwt jwt) {
        Player player = playerInternalService.findById(playerId);
        if (player.isFlagged()) {
            player.setFlagged(false);
            logInternalService.create(Type.PLAYER, jwt, "Unflagged player [username={}, id={}].", player.getUsername(),
                    player.getId());
        } else if (!player.isFlagged()) {
            player.setFlagged(true);
            logInternalService.create(Type.PLAYER, jwt, "Flagged player [username={}, id={}].", player.getUsername(),
                    player.getId());
        }
        playerRepository.save(player);
    }

    @Override
    public void addBadge(UUID playerId, UUID badgeId, Jwt jwt) {
        Player player = playerInternalService.findById(playerId);
        Badge badge = badgeInternalService.findById(badgeId);
        if (player.getBadges().contains(badge)) {
            throw new IllegalArgumentException("Badge already exists in the player's badge list.");
        }
        player.getBadges().add(badge);
        logInternalService.create(Type.PLAYER, jwt, "Added badge [name={}, id={}] to player [username={}, id={}].",
                badge.getName(), badge.getId(), player.getUsername(), player.getId());
        playerRepository.save(player);
    }

    @Override
    public void removeBadge(UUID playerId, UUID badgeId, Jwt jwt) {
        Player player = playerInternalService.findById(playerId);
        Badge badge = badgeInternalService.findById(badgeId);
        if (!player.getBadges().contains(badge)) {
            throw new IllegalArgumentException("Badge does not exist in the player's badge list.");
        }
        player.getBadges().remove(badge);
        logInternalService.create(Type.PLAYER, jwt, "Removed badge [name={}, id={}] to player [username={}, id={}].",
                badge.getName(), badge.getId(), player.getUsername(), player.getId());
        playerRepository.save(player);
    }

    @Override
    public String uploadAvatar(MultipartFile file, Jwt jwt) {
        try {
            UUID playerId = playerInternalService.getAuthenticatedPlayer(jwt).getId();
            String fileId = UUID.randomUUID().toString();

            Map<String, String> metadata = new HashMap<>();
            metadata.put("original-filename", file.getOriginalFilename());
            metadata.put("player", playerId.toString());

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileId)
                    .contentType(file.getContentType())
                    .metadata(metadata)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
            
            String avatarLink = String.format("http://localhost:9000/%s/%s", bucket, fileId);
            Player player = playerInternalService.findById(playerId);
            player.setAvatar(avatarLink);
            playerRepository.save(player);
            return avatarLink;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }
    }
}
