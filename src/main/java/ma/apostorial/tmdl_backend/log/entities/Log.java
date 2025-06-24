package ma.apostorial.tmdl_backend.log.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.log.enums.Type;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Entity @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Player author;

    private String content;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private Type type;

    @PrePersist
    public void PrePersist() {
        date = LocalDateTime.now();
    }
}
