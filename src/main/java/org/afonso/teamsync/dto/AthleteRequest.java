package org.afonso.teamsync.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AthleteRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must be at most 150 characters")
    private String name;
    private LocalDate birthDay;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;
    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phone;
    @Size(max = 100, message = "License must be at most 100 characters")
    private String license;
    @Size(max = 100, message = "Nationality must be at most 100 characters")
    private String nationality;
    private UUID addressId;
}