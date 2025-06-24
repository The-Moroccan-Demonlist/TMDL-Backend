package ma.apostorial.tmdl_backend.player.controllers.open;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.player.dtos.PlayerPostLoginRequest;
import ma.apostorial.tmdl_backend.player.dtos.PlayerProfileResponse;
import ma.apostorial.tmdl_backend.player.dtos.PlayerQueryResponse;
import ma.apostorial.tmdl_backend.player.services.interfaces.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController @RequiredArgsConstructor @RequestMapping("/api/public/players")
public class PublicPlayerController {
    private final PlayerService playerService;

    @PostMapping("/register-if-not-exists")
    public ResponseEntity<Void> postMethodName(@RequestBody PlayerPostLoginRequest request) {
        playerService.createIfNotExists(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerProfileResponse> findById(@PathVariable UUID playerId) {
        return new ResponseEntity<>(playerService.findById(playerId), HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<PlayerProfileResponse> findByUsername(@PathVariable String username) {
        return new ResponseEntity<>(playerService.findByUsername(username), HttpStatus.OK);
    }
    
    @GetMapping("/leaderboard/classic")
    public ResponseEntity<List<PlayerQueryResponse>> findAllByClassicPoints() {
        return new ResponseEntity<>(playerService.findAllByClassicPoints(), HttpStatus.OK);
    }

    @GetMapping("/leaderboard/platformer")
    public ResponseEntity<List<PlayerQueryResponse>> findAllByPlatformerPoints() {
        return new ResponseEntity<>(playerService.findAllByPlatformerPoints(), HttpStatus.OK);
    }

    // @GetMapping("/staff")
    // public ResponseEntity<List<PlayerQueryResponse>> findAllStaff() {
    //     return new ResponseEntity<>(playerService.findAllStaff(), HttpStatus.OK);
    // }
}