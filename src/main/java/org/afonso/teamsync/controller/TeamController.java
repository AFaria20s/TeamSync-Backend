package org.afonso.teamsync.controller;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.security.AuthUtils;
import org.afonso.teamsync.service.TeamService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    private final AuthUtils authUtils;

    @GetMapping
    public Team getMyTeam() {
        return teamService.getByTeamId(authUtils.getAuthenticatedTeamId());
    }

    @PutMapping("/update")
    public Team updateMyTeam(@RequestBody Team team) {
        return teamService.update(team, authUtils.getAuthenticatedTeamId());
    }

    @DeleteMapping("/delete")
    public void deleteMyTeam() {
        teamService.delete(authUtils.getAuthenticatedTeamId());
    }
}