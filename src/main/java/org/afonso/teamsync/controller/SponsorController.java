package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.SponsorRequest;
import org.afonso.teamsync.entity.Sponsor;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.SponsorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sponsors")
@RequiredArgsConstructor
public class SponsorController {
    private final SponsorService service;
    private final AuthUtils auth;

    @GetMapping
    public List<Sponsor> getAll() {
        return service.getAllFromTeam(auth.getAuthenticatedTeamId());
    }

    @GetMapping("/{id}")
    public Sponsor getById(@PathVariable UUID id) {
        return service.getById(id, auth.getAuthenticatedTeamId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sponsor create(@RequestBody SponsorRequest request) {
        return service.create(request, auth.getAuthenticatedTeamId());
    }

    @PutMapping("/{id}")
    public Sponsor update(@PathVariable UUID id, @RequestBody SponsorRequest request) {
        return service.update(id, request, auth.getAuthenticatedTeamId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id, auth.getAuthenticatedTeamId());
    }
}
