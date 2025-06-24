package ma.apostorial.tmdl_backend.changelog.controllers.open;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.changelog.dtos.ChangelogResponse;
import ma.apostorial.tmdl_backend.changelog.enums.Category;
import ma.apostorial.tmdl_backend.changelog.services.interfaces.ChangelogService;

@RestController @RequiredArgsConstructor @RequestMapping("/api/public/changelogs")
public class PublicChangelogController {
    private final ChangelogService changelogService;
    
    @GetMapping
    public ResponseEntity<Page<ChangelogResponse>> query(
            @RequestParam(required = false) String query,
            @RequestParam Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(changelogService.query(query, category, page, size), HttpStatus.OK);
    }
}
