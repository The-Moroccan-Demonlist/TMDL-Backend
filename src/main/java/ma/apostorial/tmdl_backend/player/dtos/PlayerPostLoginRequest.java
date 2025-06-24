package ma.apostorial.tmdl_backend.player.dtos;

public record PlayerPostLoginRequest(
    String sub,
    String email
) { }
