package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.CompetitionRequest;
import org.afonso.teamsync.entity.Competition;
import org.afonso.teamsync.entity.Discipline;
import org.afonso.teamsync.repository.CompetitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetitionService {
    private final CompetitionRepository repo;

    public List<Competition> getAll() {
        return repo.findAll();
    }

    public Competition getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
    }

    public Competition create(CompetitionRequest request) {
        Competition competition = new Competition();
        competition.setName(request.getName());
        competition.setLocation(request.getLocation());
        competition.setCompetitionDate(request.getCompetitionDate());

        Discipline discipline = new Discipline();
        discipline.setId(request.getDisciplineId());
        competition.setDiscipline(discipline);

        return repo.save(competition);
    }

    public Competition update(UUID id, CompetitionRequest request) {
        Competition competition = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));

        if (request.getName() != null) competition.setName(request.getName());
        if (request.getLocation() != null) competition.setLocation(request.getLocation());
        if (request.getCompetitionDate() != null) competition.setCompetitionDate(request.getCompetitionDate());

        if (request.getDisciplineId() != null) {
            Discipline discipline = new Discipline();
            discipline.setId(request.getDisciplineId());
            competition.setDiscipline(discipline);
        }

        return repo.save(competition);
    }

    public void delete(UUID id) {
        Competition competition = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found"));
        repo.delete(competition);
    }
}
