package ma.apostorial.tmdl_backend.level.entities;

import java.util.ArrayList;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.level.enums.Difficulty;
import ma.apostorial.tmdl_backend.level.enums.Duration;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(indexes = {
    @Index(name = "idx_classic_ingameId", columnList = "ingameId")
})
public class ClassicLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String ingameId;

    private String name;

    private String publisher;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private Duration duration;

    private String videoLink;

    private String thumbnailLink;

    private Double points;

    private Double minimumPoints;

    @Transient
    private Double oldPoints;

    @Transient
    private Double oldMinimumPoints;

    private int ranking;

    private int minimumCompletion;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player firstVictor;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassicRecord> records;

    @PrePersist
    public void PrePersist() {
        records = new ArrayList<>();
    }

    public void calculatePoints() {
        if (this.getRanking() == 0 || this.getRanking() > 150) {
            this.setPoints(0.0);
            this.setMinimumPoints(0.0);
        } else {
            Double points = 500 * (1 - Math.log(this.getRanking()) / Math.log(151));
            this.setPoints(Math.round(points * 100) / 100.0);
            this.setMinimumPoints(Math.round(points / 3 * 100) / 100.0);
        }
    }
}
