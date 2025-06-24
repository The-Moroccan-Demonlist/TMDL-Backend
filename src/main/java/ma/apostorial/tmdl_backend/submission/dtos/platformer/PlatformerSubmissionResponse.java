package ma.apostorial.tmdl_backend.submission.dtos.platformer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelDTOResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerResponse;
import ma.apostorial.tmdl_backend.submission.enums.Status;

public record PlatformerSubmissionResponse(
    UUID id,
    PlayerResponse player,
    PlatformerLevelDTOResponse level,
    String videoLink,
    String rawFootage,
    String comment,
    LocalDateTime submissionDate,
    LocalDateTime approvalDate,
    Status status,
    Duration recordTime
) { }
