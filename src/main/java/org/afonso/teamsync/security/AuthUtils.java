package org.afonso.teamsync.security;

import org.afonso.teamsync.entity.Manager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class AuthUtils {

    public UUID getAuthenticatedTeamId() {
        Manager manager = (Manager) Objects.requireNonNull(SecurityContextHolder.getContext()
                .getAuthentication()).getPrincipal();
        assert manager != null;
        return manager.getTeam().getId();
    }

    public Manager getAuthenticatedManager() {
        return (Manager) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}