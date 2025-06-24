package ma.apostorial.tmdl_backend.common.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.common.dtos.TokenResponse;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class OAuthController {

    @Value("${SPRING_BOOT_URL}")
    private String springBootUrl;

    @Value("${NEXTJS_URL}")
    private String nextjsUrl;

    @Value("${AUTH0_CLIENT_ID}")
    private String clientId;

    @Value("${AUTH0_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${AUTH0_DOMAIN}")
    private String domain;

    @GetMapping("/oauth/login")
    public void login(HttpServletResponse response) throws IOException {
        String redirectUri = URLEncoder.encode(springBootUrl + "/api/public/oauth/callback", StandardCharsets.UTF_8);
        String audience = URLEncoder.encode(springBootUrl, StandardCharsets.UTF_8);

        String authUrl = "https://" + domain + "/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&scope=openid profile email offline_access"
                + "&audience=" + audience
                + "&connection=google-oauth2";

        response.sendRedirect(authUrl);
    }

    @GetMapping("/oauth/callback")
    public void callback(@RequestParam String code, HttpServletResponse response) throws IOException {
        TokenResponse tokenResponse = exchangeCodeForToken(code);

        ResponseCookie accessToken = ResponseCookie.from("access_token", tokenResponse.access_token())
                .httpOnly(true).secure(true).path("/").sameSite("Lax").maxAge(300).build();

        ResponseCookie refreshToken = ResponseCookie.from("refresh_token", tokenResponse.refresh_token())
                .httpOnly(true).secure(true).path("/").sameSite("Lax").maxAge(30 * 24 * 3600).build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

        response.sendRedirect(nextjsUrl);
    }

    public TokenResponse exchangeCodeForToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://" + domain)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String body = "grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&code=" + code
                + "&redirect_uri=" + springBootUrl + "/api/public/oauth/callback";

        try {
            return webClient.post()
                    .uri("/oauth/token")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            System.out.println("Auth0 Error: " + e.getResponseBodyAsString());
            throw e;
        }
    }

    @GetMapping("/oauth/logout")
    public void logout(HttpServletResponse response) throws IOException {
        ResponseCookie accessToken = ResponseCookie.from("access_token", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();

        ResponseCookie refreshToken = ResponseCookie.from("refresh_token", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

        response.sendRedirect(nextjsUrl);
    }

    @GetMapping("/oauth/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(domain)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String body = "grant_type=refresh_token"
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&refresh_token=" + refreshToken;

        TokenResponse tokenResponse = webClient.post()
                .uri("/oauth/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();

        ResponseCookie newAccessToken = ResponseCookie.from("access_token", tokenResponse.access_token())
                .httpOnly(true).secure(true).path("/").sameSite("Lax").maxAge(3600).build();

        ResponseCookie newRefreshToken = ResponseCookie.from("refresh_token", tokenResponse.refresh_token())
                .httpOnly(true).secure(true).path("/").sameSite("Lax").maxAge(30 * 24 * 3600).build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, newAccessToken.toString());
        headers.add(HttpHeaders.SET_COOKIE, newRefreshToken.toString());

        return ResponseEntity.ok().headers(headers).body("Refreshed");
    }

}
