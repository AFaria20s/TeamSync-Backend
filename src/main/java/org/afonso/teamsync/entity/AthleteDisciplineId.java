package org.afonso.teamsync.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class AthleteDisciplineId implements Serializable {
    private static final long serialVersionUID = 8389007547979393758L;
    @NotNull
    @Column(name = "athlete_id", nullable = false)
    private UUID athleteId;

    @NotNull
    @Column(name = "discipline_id", nullable = false)
    private UUID disciplineId;


}