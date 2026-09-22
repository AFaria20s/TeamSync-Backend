package org.afonso.teamsync.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ResultRequest {
    @NotNull(message = "Athlete is required")
    private UUID athleteId;
    @NotNull(message = "Competition is required")
    private UUID competitionId;
    @Min(value = 1, message = "Position must be at least 1")
    private Integer position;
    private Object finishTime;
    @DecimalMin(value = "0.0", message = "Points cannot be negative")
    private BigDecimal points;
    private Boolean dnf;
}
