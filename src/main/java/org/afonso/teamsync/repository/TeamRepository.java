package org.afonso.teamsync.repository;

import org.afonso.teamsync.entity.Team;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByLocation(String location);
    List<Team> findByNameContainingIgnoreCase(String name);
    List<Team> findByCreatedAtAfter(Instant date);
}
