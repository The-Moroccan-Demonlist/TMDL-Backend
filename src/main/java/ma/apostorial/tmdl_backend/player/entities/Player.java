package ma.apostorial.tmdl_backend.player.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.badge.entities.Badge;
import ma.apostorial.tmdl_backend.record.entities.ClassicRecord;
import ma.apostorial.tmdl_backend.record.entities.PlatformerRecord;
import ma.apostorial.tmdl_backend.region.entities.Region;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(indexes = {
    @Index(name = "idx_player_username", columnList = "username"),
    @Index(name = "idx_player_classicPoints", columnList = "classicPoints"),
    @Index(name = "idx_player_platformerPoints", columnList = "platformerPoints")
})
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String sub;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    
    private String avatar;

    private Double classicPoints;
    private Double platformerPoints;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIncludeProperties({"id", "name"})
    private Region region;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClassicRecord> classicRecords;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PlatformerRecord> platformerRecords;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Badge> badges;

    private String discord;
    private String youtube;
    private String twitter;
    private String twitch;

    private boolean isActive;
    private boolean isFlagged;

    @PrePersist
    public void PrePersist() {
        classicPoints = 0.0;
        platformerPoints = 0.0;
        classicRecords = new HashSet<>();
        platformerRecords = new HashSet<>();
        isActive = false;
        isFlagged = false;
        badges = new HashSet<>();
    }
}
