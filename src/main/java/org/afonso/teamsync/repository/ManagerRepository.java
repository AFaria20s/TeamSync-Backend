package org.afonso.teamsync.repository;

import org.afonso.teamsync.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    Optional<Manager> findByEmail(String email);
    List<Manager> findByTeamId(UUID teamId);

    boolean existsByEmail(String email);
}