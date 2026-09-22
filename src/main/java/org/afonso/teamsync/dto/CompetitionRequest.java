package org.afonso.teamsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CompetitionRequest {
    @NotNull(message = "Discipline is required")
    private UUID disciplineId;
    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;
    @Size(max = 200, message = "Location must be at most 200 characters")
    private String location;
    @NotNull(message = "Competition date is required")
    private LocalDate competitionDate;
}
