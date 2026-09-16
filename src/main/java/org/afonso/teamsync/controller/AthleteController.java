package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.AthleteRequest;
import org.afonso.teamsync.entity.Athlete;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.AthleteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/athletes")
@RequiredArgsConstructor
public class AthleteController {
    private final AthleteService athleteService;
    private final AuthUtils authUtils;

    @GetMapping
    public List<Athlete> getAllAthletesFromTeam() {
        return athleteService.getAllFromTeam(authUtils.getAuthenticatedTeamId());
    }

    @GetMapping("/{id}")
    public Athlete getById(@PathVariable UUID id) {
        return athleteService.getById(id, authUtils.getAuthenticatedTeamId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Athlete create(@RequestBody AthleteRequest request) {
        return athleteService.create(request, authUtils.getAuthenticatedTeamId());
    }

    @PutMapping("/{id}")
    public Athlete update(@PathVariable UUID id, @RequestBody AthleteRequest request) {
        return athleteService.update(id, request, authUtils.getAuthenticatedTeamId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        athleteService.delete(id, authUtils.getAuthenticatedTeamId());
    }
}
