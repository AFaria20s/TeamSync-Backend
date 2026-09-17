package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ResultRequest {
    private UUID athleteId;
    private UUID competitionId;
    private Integer position;
    private Object finishTime;
    private BigDecimal points;
    private Boolean dnf;
}
