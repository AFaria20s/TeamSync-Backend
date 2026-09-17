package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.exceptions.ResourceNotFoundException;
import org.afonso.teamsync.dto.StaffRequest;
import org.afonso.teamsync.entity.Address;
import org.afonso.teamsync.entity.Staff;
import org.afonso.teamsync.entity.StaffRole;
import org.afonso.teamsync.entity.Team;
import org.afonso.teamsync.repository.StaffRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository repo;
    private final PasswordEncoder passwordEncoder;

    public List<Staff> getAllFromTeam(UUID teamId) {
        return repo.findAllByTeam_Id(teamId);
    }

    public Staff getById(UUID staffId) {
        return repo.getReferenceById(staffId);
    }

    public Staff create(StaffRequest request, UUID teamId) {
        Staff staff = new Staff();
        staff.setName(request.getName());
        staff.setBirthDay(request.getBirthDay());
        staff.setEmail(request.getEmail());
        staff.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        staff.setPhone(request.getPhone());

        Team team = new Team();
        team.setId(teamId);
        staff.setTeam(team);

        if (request.getStaffRoleId() != null) {
            StaffRole staffRole = new StaffRole();
            staffRole.setId(request.getStaffRoleId());
            staff.setStaffRole(staffRole);
        }

        if (request.getAddressId() != null) {
            Address address = new Address();
            address.setId(request.getAddressId());
            staff.setAddress(address);
        }

        return repo.save(staff);
    }

    public Staff update(UUID id, StaffRequest request, UUID teamId) {
        Staff staff = (Staff) repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found or does not belong to your team"));

        if (request.getName() != null) staff.setName(request.getName());
        if (request.getBirthDay() != null) staff.setBirthDay(request.getBirthDay());
        if (request.getEmail() != null) staff.setEmail(request.getEmail());
        if (request.getPassword() != null) staff.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        if (request.getPhone() != null) staff.setPhone(request.getPhone());

        if (request.getStaffRoleId() != null) {
            StaffRole staffRole = new StaffRole();
            staffRole.setId(request.getStaffRoleId());
            staff.setStaffRole(staffRole);
        }

        if (request.getAddressId() != null) {
            Address address = new Address();
            address.setId(request.getAddressId());
            staff.setAddress(address);
        }

        return repo.save(staff);
    }

    public void delete(UUID id, UUID teamId) {
        Staff staff = (Staff) repo.findByIdAndTeam_Id(id, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found or does not belong to your team"));
        repo.delete(staff);
    }
}
