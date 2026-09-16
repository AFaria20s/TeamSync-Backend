package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.AthleteRequest;
import org.afonso.teamsync.entity.Athlete;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.AthleteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository athleteRepo;
    private final PasswordEncoder passwordEncoder;

    public List<Athlete> getAllFromTeam(UUID teamId) {
        return athleteRepo.findAllByTeam_Id(teamId);
    }

    public Athlete getById(UUID id, UUID teamId) {
        return athleteRepo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new RuntimeException("Athlete not found or does not belong to your team"));
    }

    public Athlete create(AthleteRequest request, UUID teamId) {
        Athlete athlete = new Athlete();
        athlete.setName(request.getName());
        athlete.setBirthDay(request.getBirthDay());
        athlete.setEmail(request.getEmail());
        athlete.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        athlete.setPhone(request.getPhone());
        athlete.setLicense(request.getLicense());
        athlete.setNationality(request.getNationality());

        Team team = new Team();
        team.setId(teamId);
        athlete.setTeam(team);

        return athleteRepo.save(athlete);
    }

    public Athlete update(UUID id, AthleteRequest request, UUID teamId) {
        Athlete athlete = athleteRepo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new RuntimeException("Athlete not found or does not belong to your team"));

        athlete.setName(request.getName());
        athlete.setBirthDay(request.getBirthDay());
        athlete.setPhone(request.getPhone());
        athlete.setLicense(request.getLicense());
        athlete.setNationality(request.getNationality());

        if (request.getEmail() != null) athlete.setEmail(request.getEmail());
        if (request.getPassword() != null) athlete.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return athleteRepo.save(athlete);
    }

    public void delete(UUID id, UUID teamId) {
        Athlete athlete = athleteRepo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new RuntimeException("Athlete not found or does not belong to your team"));
        athleteRepo.delete(athlete);
    }
}
