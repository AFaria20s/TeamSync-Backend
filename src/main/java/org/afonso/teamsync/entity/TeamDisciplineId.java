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
public class TeamDisciplineId implements Serializable {
    private static final long serialVersionUID = -5752851286831490430L;
    @NotNull
    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @NotNull
    @Column(name = "discipline_id", nullable = false)
    private UUID disciplineId;


}