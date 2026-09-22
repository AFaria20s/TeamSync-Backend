package org.afonso.teamsync.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.ResultRequest;
import org.afonso.teamsync.entity.Result;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.ResultService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService service;
    private final AuthUtils auth;

    @GetMapping
    public List<Result> getAll() {
        return service.getAllFromTeam(auth.getAuthenticatedTeamId());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable UUID id) {
        return service.getById(id, auth.getAuthenticatedTeamId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result create(@Valid @RequestBody ResultRequest request) {
        return service.create(request, auth.getAuthenticatedTeamId());
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable UUID id, @Valid @RequestBody ResultRequest request) {
        return service.update(id, request, auth.getAuthenticatedTeamId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id, auth.getAuthenticatedTeamId());
    }
}
