package ma.apostorial.tmdl_backend.record.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.common.mappers.MapperHelper;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordUpdateRequest;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface PlatformerRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    PlatformerRecord fromCreationRequestToEntity(PlatformerRecordCreationRequest request);

    PlatformerRecordResponse fromEntityToResponse(PlatformerRecord entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    PlatformerRecord updateFromRequest(PlatformerRecordUpdateRequest request, @MappingTarget PlatformerRecord entity);
}
