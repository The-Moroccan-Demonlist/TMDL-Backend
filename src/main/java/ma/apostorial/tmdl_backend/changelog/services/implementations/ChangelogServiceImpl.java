package ma.apostorial.tmdl_backend.changelog.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogCreationRequest;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogResponse;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogUpdateRequest;
import ma.apostorial.tmdl_backend.changelog.entities.Changelog;
import ma.apostorial.tmdl_backend.changelog.enums.Category;
import ma.apostorial.tmdl_backend.changelog.mappers.ChangelogMapper;
import ma.apostorial.tmdl_backend.changelog.repositories.ChangelogRepository;
import ma.apostorial.tmdl_backend.changelog.services.interfaces.ChangelogService;
import ma.apostorial.tmdl_backend.changelog.services.internal.interfaces.ChangelogInternalService;
import ma.apostorial.tmdl_backend.common.exceptions.UnauthorizedException;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.player.services.internal.interfaces.PlayerInternalService;

@Service @Transactional @RequiredArgsConstructor
public class ChangelogServiceImpl implements ChangelogService{
    private final ChangelogInternalService changelogInternalService;
    private final ChangelogRepository changelogRepository;
    private final PlayerInternalService playerInternalService;
    private final LogInternalService logInternalService;
    private final ChangelogMapper changelogMapper;

    @Override
    public ChangelogResponse create(ChangelogCreationRequest request, Jwt jwt) {
        Changelog changelog = changelogMapper.fromCreationRequestToEntity(request);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        changelog.setAuthor(authenticatedPlayer);
        Changelog savedChangelog = changelogRepository.save(changelog);
        logInternalService.create(Type.CHANGELOG, jwt, "Created {} changelog [id={}].", changelog.getCategory(), changelog.getId());
        return changelogMapper.fromEntityToResponse(savedChangelog);
    }

    @Override
    public Page<ChangelogResponse> query(String query, Category cateogry, int page, int size) {
        Page<Changelog> changelogs = changelogInternalService.query(query, cateogry, page, size);
        return changelogs.map(changelogMapper::fromEntityToResponse);
    }

    @Override
    public ChangelogResponse update(UUID changelogId, ChangelogUpdateRequest request, Jwt jwt) {
        Changelog originalChangelog = changelogInternalService.findById(changelogId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if (!originalChangelog.getAuthor().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to edit this changelog.");
        }
        Changelog changelog = changelogMapper.updateFromRequest(request, originalChangelog);
        logInternalService.create(Type.CHANGELOG, jwt, "Updated {} changelog [id={}].", changelog.getCategory(), changelogId);
        return changelogMapper.fromEntityToResponse(changelog);
    }

    @Override
    public void deleteById(UUID changelogId, Jwt jwt) {
        Changelog changelog = changelogInternalService.findById(changelogId);
        Player authenticatedPlayer = playerInternalService.getAuthenticatedPlayer(jwt);
        if (!changelog.getAuthor().getId().equals(authenticatedPlayer.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this changelog.");
        }
        logInternalService.create(Type.CHANGELOG, jwt, "Deleted {} changelog [id={}].", changelog.getCategory(), changelogId);
        changelogRepository.deleteById(changelogId);
    }

    @Override
    public void togglePin(UUID changelogId, Jwt jwt) {
        Changelog changelog = changelogInternalService.findById(changelogId);
        if (changelog.isPinned()) {
            changelog.setPinned(false);
            logInternalService.create(Type.CHANGELOG, jwt, "Unpinned {} changelog [id={}].", changelog.getCategory(), changelogId);
        } else if (!changelog.isPinned()) {
            changelog.setPinned(true);
            logInternalService.create(Type.CHANGELOG, jwt, "Pinned {} changelog [id={}].", changelog.getCategory(), changelogId);
        }
        changelogRepository.save(changelog);
    }
}
