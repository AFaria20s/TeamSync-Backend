package org.afonso.teamsync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRoleRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
