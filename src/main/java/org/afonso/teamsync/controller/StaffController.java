package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.StaffRequest;
import org.afonso.teamsync.entity.Staff;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff")
public class StaffController {
    private final StaffService service;
    private final AuthUtils auth;

    @GetMapping
    public List<Staff> findAll() {
        return service.getAllFromTeam(auth.getAuthenticatedTeamId());
    }

    @GetMapping("/{id}")
    public Staff getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Staff create(@RequestBody StaffRequest request) {
        return service.create(request, auth.getAuthenticatedTeamId());
    }

    @PutMapping("/{id}")
    public Staff update(@PathVariable UUID id, @RequestBody StaffRequest request) {
        return service.update(id, request, auth.getAuthenticatedTeamId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id, auth.getAuthenticatedTeamId());
    }
}
