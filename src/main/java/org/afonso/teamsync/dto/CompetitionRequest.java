package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CompetitionRequest {
    private UUID disciplineId;
    private String name;
    private String location;
    private LocalDate competitionDate;
}
