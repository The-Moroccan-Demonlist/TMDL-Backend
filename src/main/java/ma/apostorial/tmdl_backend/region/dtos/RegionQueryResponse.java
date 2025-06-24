package ma.apostorial.tmdl_backend.region.dtos;

import java.util.Set;
import java.util.UUID;

import ma.apostorial.tmdl_backend.player.dtos.PlayerPointsResponse;

public record RegionQueryResponse(
    UUID id,
    String name,
    Double classicPoints,
    Double platformerPoints,
    Set<PlayerPointsResponse> players
) { }
