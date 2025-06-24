package ma.apostorial.tmdl_backend.level.controllers.open;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.platformer.PlatformerLevelResponse;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.services.interfaces.PlatformerLevelService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController @RequiredArgsConstructor @RequestMapping("/api/public/platformer-levels")
public class PublicPlatformerLevelController {
    private final PlatformerLevelService platformerLevelService;
    
    @GetMapping("/{levelId}")
    public ResponseEntity<PlatformerLevelResponse> findById(@PathVariable UUID levelId) {
        return new ResponseEntity<>(platformerLevelService.findById(levelId), HttpStatus.OK);
    }

    @GetMapping("/ingame-id/{ingameId}")
    public ResponseEntity<PlatformerLevelResponse> findByIngameId(@PathVariable String ingameId) {
        return new ResponseEntity<>(platformerLevelService.findByIngameId(ingameId), HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<PlatformerLevelQueryResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) String type) {
        return new ResponseEntity<>(platformerLevelService.query(query, difficulty, type), HttpStatus.OK);
    }  
}
