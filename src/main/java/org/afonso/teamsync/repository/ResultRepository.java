package org.afonso.teamsync.repository;

import org.afonso.teamsync.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, UUID> {
    List<Result> findAllByAthlete_Team_Id(UUID teamId);
    Optional<Result> findByIdAndAthlete_Team_Id(UUID id, UUID teamId);
}
