package ma.apostorial.tmdl_backend.level.entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
    @Index(name = "idx_platformer_ingameId", columnList = "ingameId")
})
public class PlatformerLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String ingameId;

    private String name;

    private String publisher;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String videoLink;

    private String thumbnailLink;

    private Double points;

    @Transient
    private Double oldPoints;

    private int ranking;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player recordHolder;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlatformerRecord> records;

    public void calculatePoints() {
        Double points = 500 * (1 - Math.log(this.getRanking()) / Math.log(151));
        this.setPoints(Math.round(points * 100) / 100.0);
    }
}