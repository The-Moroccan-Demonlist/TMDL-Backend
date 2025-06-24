package ma.apostorial.tmdl_backend.changelog.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.changelog.enums.Category;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Changelog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Player author;

    private String content;
    private Category category;
    private LocalDateTime publishedAt;
    private boolean isPinned;

    @PrePersist
    public void PrePersist() {
        publishedAt = LocalDateTime.now();
        isPinned = false;
    }
}
