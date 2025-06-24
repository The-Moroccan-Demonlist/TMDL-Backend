package ma.apostorial.tmdl_backend.record.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.common.mappers.MapperHelper;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordCreationRequest;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordUpdateRequest;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;

@Mapper(componentModel = "spring", uses = MapperHelper.class)
public interface ClassicRecordMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    ClassicRecord fromCreationRequestToEntity(ClassicRecordCreationRequest request);

    ClassicRecordResponse fromEntityToResponse(ClassicRecord entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    ClassicRecord updateFromRequest(ClassicRecordUpdateRequest request, @MappingTarget ClassicRecord entity);
}
