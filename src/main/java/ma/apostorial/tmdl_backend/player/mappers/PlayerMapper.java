package ma.apostorial.tmdl_backend.player.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.player.dtos.PlayerProfileResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerQueryResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerShortResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerUpdateRequest;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sub", ignore = true)
    @Mapping(target = "classicPoints", ignore = true)
    @Mapping(target = "platformerPoints", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "flagged", ignore = true)
    @Mapping(target = "badges", ignore = true)
    @Mapping(target = "classicRecords", ignore = true)
    @Mapping(target = "platformerRecords", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    Player updateFromRequest(PlayerUpdateRequest request, @MappingTarget Player entity);
    
    @Mapping(target = "permissions", source = "permissions")
    PlayerShortResponse fromEntityToShortResponse(Player entity, List<String> permissions);
    PlayerProfileResponse fromEntityToProfileResponse(Player entity);
    PlayerQueryResponse fromEntityToQueryResponse(Player entity);
}
