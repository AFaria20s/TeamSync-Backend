package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.RegisterRequest;
import org.afonso.teamsync.dto.RegisterResponse;
import org.afonso.teamsync.entity.Manager;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.ManagerRepository;
import org.afonso.teamsync.repository.TeamRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TeamRepository teamRepo;
    private final ManagerRepository managerRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
        UUID managerEmailToken = UUID.randomUUID();

        manager.setName(request.getManagerName());
        manager.setEmail(request.getEmail());
        manager.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        manager.setTeam(team);
        manager.setEmailVerified(false);
        manager.setVerificationToken(managerEmailToken.toString());
        manager.setVerificationTokenExpiresAt(Instant.now().plus(Duration.ofHours(24)));
        managerRepo.save(manager);

        emailService.sendVerificationEmail(
                manager.getEmail(),
                manager.getName(),
                manager.getVerificationToken()
        );

        return new RegisterResponse("Check your email to verify your account.");
    }

    @Transactional
    public void verifyEmail(String token) {
        Manager manager = managerRepo.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        if (manager.getVerificationTokenExpiresAt() == null
                || manager.getVerificationTokenExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Verification token has expired");
        }

        manager.setEmailVerified(true);
        manager.setVerificationToken(null);
        manager.setVerificationTokenExpiresAt(null);
        managerRepo.save(manager);
    }
}