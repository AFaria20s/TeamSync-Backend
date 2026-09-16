package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.entity.Manager;
import org.afonso.teamsync.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepo;
    public List<Manager> getAllByTeam(UUID teamId) {
        return managerRepo.findByTeamId(teamId);
    }

    public Manager getById(UUID id) {
        return managerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }
}