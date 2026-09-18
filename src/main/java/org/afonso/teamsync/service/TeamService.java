package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepo;

    public List<Team> getAll() {
        return teamRepo.findAll();
    }

    public Team getByTeamId(UUID id) {
        return teamRepo.getReferenceById(id);
    }

    public Team update(Team team, UUID id) {
        if (!Objects.equals(id, team.getId())) return null;
        return teamRepo.save(team);
    }

    public void delete(UUID id) {
        teamRepo.deleteById(id);
    }
}
