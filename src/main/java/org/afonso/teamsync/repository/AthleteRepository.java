package org.afonso.teamsync.repository;

import org.afonso.teamsync.entity.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, UUID> {
    List<Athlete> findAllByTeam_Id(UUID teamId);
    Optional<Athlete> findByIdAndTeam_Id(UUID id, UUID teamId);
}
