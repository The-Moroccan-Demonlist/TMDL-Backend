package ma.apostorial.tmdl_backend.level.controllers.open;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelQueryResponse;
import ma.apostorial.tmdl_backend.level.dtos.classic.ClassicLevelResponse;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.level.services.interfaces.ClassicLevelService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController @RequiredArgsConstructor @RequestMapping("/api/public/classic-levels")
public class PublicClassicLevelController {
    private final ClassicLevelService classicLevelService;
    
    @GetMapping("/{levelId}")
    public ResponseEntity<ClassicLevelResponse> findById(@PathVariable UUID levelId) {
        return new ResponseEntity<>(classicLevelService.findById(levelId), HttpStatus.OK);
    }

    @GetMapping("/ingame-id/{ingameId}")
    public ResponseEntity<ClassicLevelResponse> findByIngameId(@PathVariable String ingameId) {
        return new ResponseEntity<>(classicLevelService.findByIngameId(ingameId), HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<List<ClassicLevelQueryResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) Duration duration,
            @RequestParam(required = false) String type) {
        return new ResponseEntity<>(classicLevelService.query(query, difficulty, duration, type), HttpStatus.OK);
    }  
}
