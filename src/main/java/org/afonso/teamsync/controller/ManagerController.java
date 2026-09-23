package org.afonso.teamsync.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.ManagerProfilePictureRequest;
import org.afonso.teamsync.dto.ManagerRequest;
import org.afonso.teamsync.dto.ManagerResponse;
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
    public ManagerResponse getManagerInfo() {
        return ManagerResponse.from(managerService.getById(authUtils.getAuthenticatedManager().getId()));
    }

    @PutMapping("/update")
    public ManagerResponse updateManager(@Valid @RequestBody ManagerRequest request) {
        return managerService.update(authUtils.getAuthenticatedManager().getId(), request);
    }

    @PutMapping("/profile-picture")
    public ManagerResponse updateProfilePicture(
            @Valid @RequestBody ManagerProfilePictureRequest request
    ) {
        return managerService.updateProfilePicture(
                authUtils.getAuthenticatedManager().getId(),
                request
        );
    }

    @DeleteMapping("/profile-picture")
    public ManagerResponse removeProfilePicture() {
        return managerService.removeProfilePicture(authUtils.getAuthenticatedManager().getId());
    }
}