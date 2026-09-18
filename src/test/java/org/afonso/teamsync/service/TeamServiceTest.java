package org.afonso.teamsync.service;

import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepo;

    @InjectMocks
    private TeamService teamService;

    @Test
    void getAllShouldReturnAllTeams() {
        List<Team> teams = List.of(team("Team One"), team("Team Two"));
        when(teamRepo.findAll()).thenReturn(teams);

        List<Team> result = teamService.getAll();

        assertThat(result).isSameAs(teams);
        verify(teamRepo).findAll();
    }

    @Test
    void getByTeamIdShouldReturnReferencedTeam() {
        UUID teamId = UUID.randomUUID();
        Team team = team("Team One");
        team.setId(teamId);
        when(teamRepo.getReferenceById(teamId)).thenReturn(team);

        Team result = teamService.getByTeamId(teamId);

        assertThat(result).isSameAs(team);
        verify(teamRepo).getReferenceById(teamId);
    }

    @Test
    void updateShouldSaveTeamWhenIdsMatch() {
        UUID teamId = UUID.randomUUID();
        Team team = team("Updated team");
        team.setId(teamId);
        when(teamRepo.save(team)).thenReturn(team);

        Team result = teamService.update(team, teamId);

        assertThat(result).isSameAs(team);
        verify(teamRepo).save(team);
    }

    @Test
    void updateShouldReturnNullAndNotSaveWhenIdsDoNotMatch() {
        UUID requestedId = UUID.randomUUID();
        Team team = team("Team One");
        team.setId(UUID.randomUUID());

        Team result = teamService.update(team, requestedId);

        assertThat(result).isNull();
        verify(teamRepo, org.mockito.Mockito.never()).save(team);
    }

    @Test
    void deleteShouldDeleteTeamById() {
        UUID teamId = UUID.randomUUID();

        teamService.delete(teamId);

        verify(teamRepo).deleteById(teamId);
    }

    private Team team(String name) {
        Team team = new Team();
        team.setName(name);
        return team;
    }
}
