package ma.apostorial.tmdl_backend.record.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.level.entities.PlatformerLevel;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_id", "level_id"})
    }
)
public class PlatformerRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String videoLink;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIncludeProperties({"id", "username"})
    private Player player;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIncludeProperties({"id", "name"})
    private PlatformerLevel level;

    private Duration recordTime;

    LocalDateTime completedAt;

    @PrePersist
    public void PrePersist() {
        completedAt = LocalDateTime.now();
    }
}
