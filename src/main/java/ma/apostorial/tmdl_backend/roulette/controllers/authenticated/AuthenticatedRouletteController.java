package ma.apostorial.tmdl_backend.roulette.controllers.authenticated;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteCreationRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteDoneRequest;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResultResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteQueryResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteResponse;
import ma.apostorial.tmdl_backend.roulette.dtos.RouletteUpdateRequest;
import ma.apostorial.tmdl_backend.roulette.services.interfaces.RouletteService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController @RequiredArgsConstructor @RequestMapping("/api/authenticated/roulettes")
public class AuthenticatedRouletteController {
    private final RouletteService rouletteService;

    @PostMapping("/create")
    public ResponseEntity<RouletteResponse> create(
            @Valid @RequestBody RouletteCreationRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.create(request, jwt), HttpStatus.OK);
    }
    
    @GetMapping("/{rouletteId}")
    public ResponseEntity<RouletteResponse> findById(
            @PathVariable UUID rouletteId,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.findById(rouletteId, jwt), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RouletteQueryResponse>> findAllByPlayer(
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.findAllByPlayer(jwt), HttpStatus.OK);
    }

    @PatchMapping("/update/{rouletteId}")
    public ResponseEntity<RouletteResponse> update(
            @PathVariable UUID rouletteId ,
            @Valid @RequestBody RouletteUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.update(rouletteId, request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/delete/{rouletteId}")
    public ResponseEntity<RouletteResponse> deleteById(
            @PathVariable UUID rouletteId,
            @AuthenticationPrincipal Jwt jwt) {
        rouletteService.deleteById(rouletteId, jwt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/done")
    public ResponseEntity<RouletteResultResponse> markDone(
            @Valid @RequestBody RouletteDoneRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.markDone(request, jwt), HttpStatus.OK);
    }

    @PatchMapping("/{rouletteId}/give-up")
    public ResponseEntity<RouletteResultResponse> giveUp(
            @PathVariable UUID rouletteId,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.giveUp(rouletteId, jwt), HttpStatus.OK);
    }

    @PostMapping("/{rouletteId}")
    public ResponseEntity<RouletteResponse> clone(
            @PathVariable UUID rouletteId,
            @RequestParam String name,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(rouletteService.clone(rouletteId, name, jwt), HttpStatus.OK);
    }
}
