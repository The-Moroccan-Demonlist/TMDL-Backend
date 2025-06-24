package ma.apostorial.tmdl_backend.roulette.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.roulette.dtos.RouletteCreationRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteQueryResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResultResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteUpdateRequest;
import ma.apostorial.tmdl_backend.roulette.entities.Roulette;

@Mapper(componentModel = "spring")
public interface RouletteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classicLevels", ignore = true)
    @Mapping(target = "currentLevel", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "count", ignore = true)
    @Mapping(target = "isCompleted", ignore = true)
    @Mapping(target = "isGivenUp", ignore = true)
    @Mapping(target = "percentages", ignore = true)
    Roulette fromCreationRequestToEntity(RouletteCreationRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classicLevels", ignore = true)
    @Mapping(target = "completed", ignore = true)
    @Mapping(target = "givenUp", ignore = true)
    @Mapping(target = "count", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "currentLevel", ignore = true)
    @Mapping(target = "percentages", ignore = true)
    Roulette updateFromRequest(RouletteUpdateRequest request, @MappingTarget Roulette entity);

    RouletteResponse fromEntityToResponse(Roulette entity);
    RouletteQueryResponse fromEntityToQueryResponse(Roulette entity);
    RouletteResultResponse fromEntityToResultResponse(Roulette entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isGivenUp", ignore = true)
    @Mapping(target = "isCompleted", ignore = true)
    @Mapping(target = "currentLevel", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "percentages", ignore = true)
    Roulette clone(Roulette toCopy);
}
