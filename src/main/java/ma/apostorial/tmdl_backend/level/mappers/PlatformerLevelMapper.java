package ma.apostorial.tmdl_backend.level.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.common.mappers.MapperHelper;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface PlatformerLevelMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "recordHolder", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "oldPoints", ignore = true)
    @Mapping(target = "ranking", ignore = true)
    @Mapping(target = "records", ignore = true)
    PlatformerLevel fromCreationRequestToEntity(PlatformerLevelCreationRequest request);

    PlatformerLevelResponse fromEntityToResponse(PlatformerLevel entity);
    PlatformerLevelQueryResponse fromEntityToQueryResponse(PlatformerLevel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "oldPoints", ignore = true)
    @Mapping(target = "ranking", ignore = true)
    @Mapping(target = "records", ignore = true)
    PlatformerLevel updateFromRequest(PlatformerLevelUpdateRequest request, @MappingTarget PlatformerLevel entity);
}
