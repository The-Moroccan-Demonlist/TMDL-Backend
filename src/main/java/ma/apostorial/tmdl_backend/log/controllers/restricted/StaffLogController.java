package ma.apostorial.tmdl_backend.log.controllers.restricted;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ma.apostorial.tmdl_backend.log.dtos.LogResponse;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.log.services.interfaces.LogService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController @RequiredArgsConstructor @RequestMapping("/api/staff/logs")
public class StaffLogController {
    private final LogService logService;

    @GetMapping
    public ResponseEntity<Page<LogResponse>> query(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) Type type,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(logService.query(query, type, page, size), HttpStatus.OK);
    }
}
