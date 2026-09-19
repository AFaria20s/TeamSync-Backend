package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.RegisterRequest;
import org.afonso.teamsync.dto.RegisterResponse;
import org.afonso.teamsync.entity.Manager;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.ManagerRepository;
import org.afonso.teamsync.repository.TeamRepository;
import org.afonso.teamsync.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TeamRepository teamRepo;
    private final ManagerRepository managerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // verify if email is already registered
        if (managerRepo.existsByEmail((request.getEmail()))) {
            throw new RuntimeException("Email already in use");
        }

        // create team
        Team team = new Team();
        team.setName(request.getTeamName());
        teamRepo.save(team);

        // create manager and encrypt password using BCrypt
        Manager manager = new Manager();
        manager.setName(request.getManagerName());
        manager.setEmail(request.getEmail());
        manager.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        manager.setTeam(team);
        managerRepo.save(manager);

        // generate JWT
        String token = jwtService.generateToken(manager);

        // return RegisterResponse
        return new RegisterResponse(token);
    }
}