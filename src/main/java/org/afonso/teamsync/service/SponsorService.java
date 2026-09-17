package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.SponsorRequest;
import org.afonso.teamsync.entity.Address;
import org.afonso.teamsync.entity.Sponsor;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.SponsorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SponsorService {
    private final SponsorRepository repo;

    public List<Sponsor> getAllFromTeam(UUID teamId) {
        return repo.findAllByTeam_Id(teamId);
    }

    public Sponsor getById(UUID id, UUID teamId) {
        return repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found or does not belong to your team"));
    }

    public Sponsor create(SponsorRequest request, UUID teamId) {
        Sponsor sponsor = new Sponsor();
        sponsor.setName(request.getName());
        sponsor.setEmail(request.getEmail());
        sponsor.setPhone(request.getPhone());
        sponsor.setWebsite(request.getWebsite());
        sponsor.setSponsorType(request.getSponsorType());
        sponsor.setStatus(request.getStatus());
        sponsor.setStartDate(request.getStartDate());
        sponsor.setEndDate(request.getEndDate());

        Team team = new Team();
        team.setId(teamId);
        sponsor.setTeam(team);

        if (request.getAddressId() != null) {
            Address address = new Address();
            address.setId(request.getAddressId());
            sponsor.setAddress(address);
        }

        return repo.save(sponsor);
    }

    public Sponsor update(UUID id, SponsorRequest request, UUID teamId) {
        Sponsor sponsor = repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found or does not belong to your team"));

        if (request.getName() != null) sponsor.setName(request.getName());
        if (request.getEmail() != null) sponsor.setEmail(request.getEmail());
        if (request.getPhone() != null) sponsor.setPhone(request.getPhone());
        if (request.getWebsite() != null) sponsor.setWebsite(request.getWebsite());
        if (request.getSponsorType() != null) sponsor.setSponsorType(request.getSponsorType());
        if (request.getStatus() != null) sponsor.setStatus(request.getStatus());
        if (request.getStartDate() != null) sponsor.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) sponsor.setEndDate(request.getEndDate());

        if (request.getAddressId() != null) {
            Address address = new Address();
            address.setId(request.getAddressId());
            sponsor.setAddress(address);
        }

        return repo.save(sponsor);
    }

    public void delete(UUID id, UUID teamId) {
        Sponsor sponsor = repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found or does not belong to your team"));
        repo.delete(sponsor);
    }
}
