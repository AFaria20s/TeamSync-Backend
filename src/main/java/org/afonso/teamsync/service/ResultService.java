package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.ResultRequest;
import org.afonso.teamsync.entity.Athlete;
import org.afonso.teamsync.entity.Competition;
import org.afonso.teamsync.entity.Result;
import org.afonso.teamsync.repository.AthleteRepository;
import org.afonso.teamsync.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository repo;
    private final AthleteRepository athleteRepo;

    public List<Result> getAllFromTeam(UUID teamId) {
        return repo.findAllByAthlete_Team_Id(teamId);
    }

    public Result getById(UUID id, UUID teamId) {
        return repo.findByIdAndAthlete_Team_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found or does not belong to your team"));
    }

    public Result create(ResultRequest request, UUID teamId) {
        Result result = new Result();
        result.setPosition(request.getPosition());
        result.setFinishTime(request.getFinishTime());
        result.setPoints(request.getPoints());
        result.setDnf(request.getDnf());

        Athlete athlete = athleteRepo.findByIdAndTeam_Id(request.getAthleteId(), teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Athlete not found or does not belong to your team"));
        result.setAthlete(athlete);

        Competition competition = new Competition();
        competition.setId(request.getCompetitionId());
        result.setCompetition(competition);

        return repo.save(result);
    }

    public Result update(UUID id, ResultRequest request, UUID teamId) {
        Result result = repo.findByIdAndAthlete_Team_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found or does not belong to your team"));

        if (request.getPosition() != null) result.setPosition(request.getPosition());
        if (request.getFinishTime() != null) result.setFinishTime(request.getFinishTime());
        if (request.getPoints() != null) result.setPoints(request.getPoints());
        if (request.getDnf() != null) result.setDnf(request.getDnf());

        if (request.getAthleteId() != null) {
            Athlete athlete = athleteRepo.findByIdAndTeam_Id(request.getAthleteId(), teamId)
                    .orElseThrow(() -> new ResourceNotFoundException("Athlete not found or does not belong to your team"));
            result.setAthlete(athlete);
        }

        if (request.getCompetitionId() != null) {
            Competition competition = new Competition();
            competition.setId(request.getCompetitionId());
            result.setCompetition(competition);
        }

        return repo.save(result);
    }

    public void delete(UUID id, UUID teamId) {
        Result result = repo.findByIdAndAthlete_Team_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found or does not belong to your team"));
        repo.delete(result);
    }
}
