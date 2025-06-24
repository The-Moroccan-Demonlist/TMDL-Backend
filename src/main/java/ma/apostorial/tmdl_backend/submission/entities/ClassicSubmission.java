package ma.apostorial.tmdl_backend.submission.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.apostorial.tmdl_backend.level.entities.ClassicLevel;
import ma.apostorial.tmdl_backend.player.entities.Player;
import ma.apostorial.tmdl_backend.submission.enums.Status;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClassicSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIncludeProperties({"id", "name"})
    private Player player;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIncludeProperties({"id", "name"})
    private ClassicLevel level;

    private String unexistantIngameId;

    private String videoLink;
    private String rawFootage;

    private String comment;

    private LocalDateTime submissionDate;
    private LocalDateTime approvalDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int recordPercentage;

    @PrePersist
    public void PrePersist() {
        submissionDate = LocalDateTime.now();
        status = Status.PENDING;
    }
}
