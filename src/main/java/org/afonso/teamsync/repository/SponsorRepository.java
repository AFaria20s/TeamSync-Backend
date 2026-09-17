package org.afonso.teamsync.repository;

import org.afonso.teamsync.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, UUID> {
    List<Sponsor> findAllByTeam_Id(UUID teamId);
    Optional<Sponsor> findByIdAndTeam_Id(UUID id, UUID teamId);
}
