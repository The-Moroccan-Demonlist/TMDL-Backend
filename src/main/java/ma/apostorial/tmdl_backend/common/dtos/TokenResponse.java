package ma.apostorial.tmdl_backend.common.dtos;

public record TokenResponse(
    String access_token,
    String refresh_token,
    String id_token,
    String token_type,
    int expires_in
) { }
