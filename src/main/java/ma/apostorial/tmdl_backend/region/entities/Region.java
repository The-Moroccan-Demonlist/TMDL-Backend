package ma.apostorial.tmdl_backend.region.entities;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.player.entities.Player;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Region {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private Double classicPoints;
    private Double platformerPoints;

    @OneToMany(mappedBy = "region")
    private Set<Player> players;

    @PrePersist
    public void PrePersist() {
        classicPoints = 0.0;
        platformerPoints = 0.0;
    }
}
