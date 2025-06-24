package ma.apostorial.tmdl_backend.level.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.common.mappers.MapperHelper;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelCreationRequest;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelUpdateRequest;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface ClassicLevelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstVictor", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "minimumPoints", ignore = true)
    @Mapping(target = "oldPoints", ignore = true)
    @Mapping(target = "oldMinimumPoints", ignore = true)
    @Mapping(target = "records", ignore = true)
    ClassicLevel fromCreationRequestToEntity(ClassicLevelCreationRequest request);

    ClassicLevelResponse fromEntityToResponse(ClassicLevel entity);
    ClassicLevelQueryResponse fromEntityToQueryResponse(ClassicLevel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "minimumPoints", ignore = true)
    @Mapping(target = "oldPoints", ignore = true)
    @Mapping(target = "oldMinimumPoints", ignore = true)
    @Mapping(target = "ranking", ignore = true)
    @Mapping(target = "records", ignore = true)
    ClassicLevel updateFromRequest(ClassicLevelUpdateRequest request, @MappingTarget ClassicLevel entity);
}
