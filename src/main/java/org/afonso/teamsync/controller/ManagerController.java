package org.afonso.teamsync.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.ManagerRequest;
import org.afonso.teamsync.entity.Manager;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;
    private final AuthUtils authUtils;

    @GetMapping
    public Manager getManagerInfo() {
        return managerService.getById(authUtils.getAuthenticatedManager().getId());
    }

    @PutMapping("/update")
    public Manager updateManager(@Valid @RequestBody ManagerRequest request) {
        return managerService.update(authUtils.getAuthenticatedManager().getId(), request);
    }
}