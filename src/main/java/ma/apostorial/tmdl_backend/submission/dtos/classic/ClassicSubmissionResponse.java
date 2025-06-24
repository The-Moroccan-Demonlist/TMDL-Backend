package ma.apostorial.tmdl_backend.submission.dtos.classic;

import java.time.LocalDateTime;
import java.util.UUID;

import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelDTOResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;
import ma.apostorial.tmdl_backend.submission.enums.Status;

public record ClassicSubmissionResponse(
    UUID id,
    PlayerResponse player,
    ClassicLevelDTOResponse level,
    String videoLink,
    String rawFootage,
    String comment,
    LocalDateTime submissionDate,
    LocalDateTime approvalDate,
    Status status,
    int recordPercentage
) { }
