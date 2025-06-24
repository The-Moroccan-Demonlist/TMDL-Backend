package ma.apostorial.tmdl_backend.player.dtos;

import java.util.Set;
import java.util.UUID;

import ma.apostorial.tmdl_backend.region.dtos.RegionPlayerResponse;
import ma.apostorial.tmdl_backend.badge.entities.Badge;
import ma.apostorial.tmdl_backend.record.dtos.classic.ClassicRecordResponse;
import ma.apostorial.tmdl_backend.record.dtos.platformer.PlatformerRecordResponse;

public record PlayerProfileResponse(
    UUID id,
    String username,
    RegionPlayerResponse region,
    String avatar,
    Double classicPoints,
    Double platformerPoints,
    Set<Badge> badges,
    Set<ClassicRecordResponse> classicRecords,
    Set<PlatformerRecordResponse> platformerRecords,
    boolean active,
    boolean flagged,
    String discord,
    String youtube,
    String twitter,
    String twitch
) { }
