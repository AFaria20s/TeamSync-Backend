package org.afonso.teamsync.repository;

import org.afonso.teamsync.entity.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRoleRepository extends JpaRepository<StaffRole, UUID> {

    List<StaffRole> findAllByTeam_Id(UUID teamId);
    Optional<StaffRole> findByIdAndTeam_Id(UUID id, UUID teamId);
}
