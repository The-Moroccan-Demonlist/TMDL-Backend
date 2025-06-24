package ma.apostorial.tmdl_backend.region.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.region.dtos.RegionCreationRequest;
import ma.apostorial.tmdl_backend.region.dtos.RegionQueryResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionResponse;
import ma.apostorial.tmdl_backend.region.dtos.RegionUpdateRequest;
import ma.apostorial.tmdl_backend.region.entities.Region;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classicPoints", ignore = true)
    @Mapping(target = "platformerPoints", ignore = true)
    @Mapping(target = "players", ignore = true)
    Region fromCreationRequestToEntity(RegionCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classicPoints", ignore = true)
    @Mapping(target = "platformerPoints", ignore = true)
    @Mapping(target = "players", ignore = true)
    Region updateFromRequest(RegionUpdateRequest request, @MappingTarget Region entity);

    RegionResponse fromEntityToResponse(Region entity);

    RegionQueryResponse fromEntityToQueryResponse(Region entity);
}
