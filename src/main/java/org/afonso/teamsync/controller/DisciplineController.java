package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.DisciplineRequest;
import org.afonso.teamsync.entity.Discipline;
import org.afonso.teamsync.service.DisciplineService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/disciplines")
@RequiredArgsConstructor
public class DisciplineController {
    private final DisciplineService service;

    @GetMapping
    public List<Discipline> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Discipline getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Discipline create(@RequestBody DisciplineRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Discipline update(@PathVariable UUID id, @RequestBody DisciplineRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
