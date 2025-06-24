package ma.apostorial.tmdl_backend.badge.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.badge.dtos.BadgeCreationRequest;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeResponse;
import ma.apostorial.tmdl_backend.badge.dtos.BadgeUpdateRequest;
import ma.apostorial.tmdl_backend.badge.entities.Badge;

@Mapper(componentModel = "spring")
public interface BadgeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Badge fromCreationRequestToEntity(BadgeCreationRequest request);

    BadgeResponse fromEntityTResponse(Badge entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Badge updateFromRequest(BadgeUpdateRequest request, @MappingTarget Badge entity);
    
}
