package ma.apostorial.tmdl_backend.badge.services.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeCreationRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeReorderRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeResponse;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeUpdateRequest;
import ma.apostorial.tmdl_backend.badge.entities.Badge;
import ma.apostorial.tmdl_backend.badge.mappers.BadgeMapper;
import ma.apostorial.tmdl_backend.badge.repositories.BadgeRepository;
import ma.apostorial.tmdl_backend.badge.services.interfaces.BadgeService;
import ma.apostorial.tmdl_backend.badge.services.internal.interfaces.BadgeInternalService;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.internal.interfaces.LogInternalService;

@Service @Transactional @RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService{
    private final BadgeInternalService badgeInternalService;
    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;
    private final LogInternalService logInternalService;

    @Override
    public BadgeResponse create(BadgeCreationRequest request, Jwt jwt) {
        Badge badge = badgeMapper.fromCreationRequestToEntity(request);
        badge.setOrder(-1);
        Badge savedBadge = badgeRepository.save(badge);
        logInternalService.create(Type.BADGE, jwt, "Created badge [name={}, id={}].", badge.getName(), badge.getId());
        return badgeMapper.fromEntityTResponse(savedBadge);
    }

    @Override
    public BadgeResponse findById(UUID badgeId) {
        Badge badge = badgeInternalService.findById(badgeId);
        return badgeMapper.fromEntityTResponse(badge);
    }

    @Override
    public List<BadgeResponse> findAll() {
        List<Badge> badges = badgeInternalService.findAll();
        return badges.stream()
            .map(badgeMapper::fromEntityTResponse)
            .toList();
    }

    @Override
    public BadgeResponse update(UUID badgeId, BadgeUpdateRequest request, Jwt jwt) {
        Badge originalBadge = badgeInternalService.findById(badgeId);
        String originalName = originalBadge.getName();
        Badge badge = badgeMapper.updateFromRequest(request, originalBadge);
        logInternalService.create(Type.BADGE, jwt, "Updated badge [name={}, id={}].", originalName, badgeId);
        return badgeMapper.fromEntityTResponse(badge);
    }

    @Override
    public void deleteById(UUID badgeId, Jwt jwt) {
        Badge badge = badgeInternalService.findById(badgeId);
        logInternalService.create(Type.BADGE, jwt, "Deleted badge [name={}, id={}].", badge.getName(), badgeId);
        badgeRepository.deleteById(badgeId);
    }

    @Override
    public void reorder(List<BadgeReorderRequest> newBadges, Jwt jwt) {
        List<UUID> badgeIds = newBadges.stream()
            .map(BadgeReorderRequest::id)
            .toList();
            
        List<Badge> badges = badgeInternalService.findAllById(badgeIds);

        Map<UUID, Integer> ordersMap = newBadges.stream()
            .collect(Collectors.toMap(BadgeReorderRequest::id, BadgeReorderRequest::order));

        List<Badge> badgesToUpdate = new ArrayList<>();
        for (Badge badge : badges) {
            int newOrder = ordersMap.get(badge.getId());
            badge.setOrder(newOrder);
            badgesToUpdate.add(badge);
        }
        badgeRepository.saveAll(badgesToUpdate);
        logInternalService.create(Type.BADGE, jwt, "Reordered {} badges.", badgesToUpdate.size());
    }
}
