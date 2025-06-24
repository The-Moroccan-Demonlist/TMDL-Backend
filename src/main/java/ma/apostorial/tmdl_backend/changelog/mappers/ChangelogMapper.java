package ma.apostorial.tmdl_backend.changelog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogCreationRequest;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogResponse;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogUpdateRequest;
import ma.apostorial.tmdl_backend.changelog.entities.Changelog;

@Mapper(componentModel = "spring")
public interface ChangelogMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "pinned", ignore = true)
    Changelog fromCreationRequestToEntity(ChangelogCreationRequest request);

    ChangelogResponse fromEntityToResponse(Changelog entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "pinned", ignore = true)
    @Mapping(target = "category", ignore = true)
    Changelog updateFromRequest(ChangelogUpdateRequest request, @MappingTarget Changelog entity);

}
