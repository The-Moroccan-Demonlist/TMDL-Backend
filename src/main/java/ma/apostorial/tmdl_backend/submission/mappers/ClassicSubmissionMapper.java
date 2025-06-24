package ma.apostorial.tmdl_backend.submission.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionCreationRequest;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionResponse;
import ma.apostorial.tmdl_backend.submission.dtos.classic.ClassicSubmissionUpdateRequest;
import ma.apostorial.tmdl_backend.submission.entities.ClassicSubmission;

@Mapper(componentModel = "spring")
public interface ClassicSubmissionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "approvalDate", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "unexistantIngameId", ignore = true)
    ClassicSubmission fromCreationRequestToEntity(ClassicSubmissionCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "submissionDate", ignore = true)
    @Mapping(target = "approvalDate", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "unexistantIngameId", ignore = true)
    @Mapping(target = "videoLink", ignore = true)
    @Mapping(target = "rawFootage", ignore = true)
    @Mapping(target = "recordPercentage", ignore = true)
    ClassicSubmission updateFromRequest(ClassicSubmissionUpdateRequest request, @MappingTarget ClassicSubmission entity);

    ClassicSubmissionResponse fromEntityToResponse(ClassicSubmission entity);
}
