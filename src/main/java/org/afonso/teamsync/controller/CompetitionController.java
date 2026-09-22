package org.afonso.teamsync.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.CompetitionRequest;
import org.afonso.teamsync.entity.Competition;
import org.afonso.teamsync.service.CompetitionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService service;

    @GetMapping
    public List<Competition> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Competition getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Competition create(@Valid @RequestBody CompetitionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Competition update(@PathVariable UUID id, @Valid @RequestBody CompetitionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
