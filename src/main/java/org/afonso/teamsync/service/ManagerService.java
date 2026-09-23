package org.afonso.teamsync.service;

import lombok.RequiredArgsConstructor;
import org.afonso.teamsync.dto.ManagerRequest;
import org.afonso.teamsync.dto.ManagerResponse;
import org.afonso.teamsync.entity.Address;
import org.afonso.teamsync.entity.Manager;
import org.afonso.teamsync.repository.ManagerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepo;
    private final PasswordEncoder passwordEncoder;
    private final SupabaseStorageService storageService;
    public List<Manager> getAllByTeam(UUID teamId) {
        return managerRepo.findByTeamId(teamId);
    }

    public Manager getById(UUID id) {
        return managerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    public ManagerResponse update(UUID id, ManagerRequest request) {
        Manager manager = getById(id);
        if (request.getName() != null) manager.setName(request.getName());
        if (request.getBirthDay() != null) manager.setBirthDay(request.getBirthDay());
        if (request.getEmail() != null) manager.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            manager.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getPhone() != null) manager.setPhone(request.getPhone());
        if (request.getAddressId() != null) {
            Address address = new Address();
            address.setId(request.getAddressId());
            manager.setAddress(address);
        }
        return ManagerResponse.from(managerRepo.save(manager));
    }

    public ManagerResponse updateProfilePicture(UUID id, MultipartFile file) {
        Manager manager = getById(id);
        manager.setProfilePicUrl(storageService.uploadManagerProfilePicture(id, file));
        return ManagerResponse.from(managerRepo.save(manager));
    }

    public ManagerResponse removeProfilePicture(UUID id) {
        Manager manager = getById(id);
        storageService.deleteManagerProfilePicture(id);
        manager.setProfilePicUrl(null);
        return ManagerResponse.from(managerRepo.save(manager));
    }

}