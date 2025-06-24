package ma.apostorial.tmdl_backend.roulette.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Entity @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Roulette {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToMany
    @JoinTable(
        name = "roulette_classic_level",
        joinColumns = @JoinColumn(name = "roulette_id"),
        inverseJoinColumns = @JoinColumn(name = "classic_level_id")
    )
    private List<ClassicLevel> classicLevels;

    @ManyToOne
    private Player player;
    private int count;
    @ElementCollection
    private List<Integer> percentages;

    private boolean isCompleted;
    private boolean isGivenUp;

    @ManyToOne
    private ClassicLevel currentLevel;

    @PrePersist
    public void PrePersist() {
        count = 0;
        percentages = new ArrayList<>(List.of(1));
        isCompleted = false;
        isGivenUp = false;
    }
}