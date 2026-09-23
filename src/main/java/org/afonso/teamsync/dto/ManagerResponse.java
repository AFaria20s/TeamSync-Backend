package org.afonso.teamsync.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.afonso.teamsync.entity.Manager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ManagerResponse {
    private UUID id;
    private UUID teamId;
    private String name;
    private LocalDate birthDay;
    private String email;
    private String phone;
    private UUID addressId;
    private Instant createdAt;
    private String profilePicUrl;

    public static ManagerResponse from(Manager manager) {
        return new ManagerResponse(
                manager.getId(),
                manager.getTeam() != null ? manager.getTeam().getId() : null,
                manager.getName(),
                manager.getBirthDay(),
                manager.getEmail(),
                manager.getPhone(),
                manager.getAddress() != null ? manager.getAddress().getId() : null,
                manager.getCreatedAt(),
                manager.getProfilePicUrl()
        );
    }
}
