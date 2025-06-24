package ma.apostorial.tmdl_backend.submission.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.platformer.PlatformerSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.entities.PlatformerSubmission;

@Mapper(componentModel = "spring")
public interface PlatformerSubmissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "approvalDate", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "unexistantIngameId", ignore = true)
    PlatformerSubmission fromCreationRequestToEntity(PlatformerSubmissionCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "approvalDate", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "unexistantIngameId", ignore = true)
    @Mapping(target = "videoLink", ignore = true)
    @Mapping(target = "rawFootage", ignore = true)
    @Mapping(target = "recordTime", ignore = true)
    PlatformerSubmission updateFromRequest(PlatformerSubmissionUpdateRequest request, @MappingTarget PlatformerSubmission entity);

    PlatformerSubmissionResponse fromEntityToResponse(PlatformerSubmission entity);
}
