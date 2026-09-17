package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.StaffRoleRequest;
import org.afonso.teamsync.entity.StaffRole;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.StaffRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffRoleService {
    private final StaffRoleRepository repo;

    public List<StaffRole> getAllRolesFromTeam(UUID teamId) {
        return repo.findAllByTeam_Id(teamId);
    }

    public StaffRole create(StaffRoleRequest roleRequest, UUID teamId) {
        StaffRole role = new StaffRole();
        role.setName(role.getName());
        role.setDescription(role.getDescription());

        Team team = new Team();
        team.setId(teamId);
        role.setTeam(team);

        return repo.save(role);
    }


    public StaffRole update(UUID id, StaffRoleRequest request, UUID teamId) {
        StaffRole role = (StaffRole) repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("StaffRole not found or does not belong to your team"));

        if (request.getName() != null) role.setName(request.getName());
        if (request.getDescription() != null) role.setDescription(request.getDescription());

        return repo.save(role);
    }

    public void delete(UUID id, UUID teamId) {
        StaffRole role = repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("StaffRole not found or does not belong to your team"));

        repo.delete(role);
    }

}
