package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.StaffRoleRequest;
import org.afonso.teamsync.entity.StaffRole;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.StaffRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/staffroles")
@RequiredArgsConstructor
public class StaffRoleController {
    private final StaffRoleService service;
    private final AuthUtils auth;

    @GetMapping
    public List<StaffRole> getAll() {
        return service.getAllRolesFromTeam(auth.getAuthenticatedTeamId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StaffRole create(@RequestBody StaffRoleRequest request) {
        return service.create(request, auth.getAuthenticatedTeamId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StaffRole update(@PathVariable UUID id, @RequestBody StaffRoleRequest request) {
        return service.update(id, request, auth.getAuthenticatedTeamId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id, auth.getAuthenticatedTeamId());
    }
}
