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
public class SponsorRequest {
    private UUID addressId;
    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name must be at most 200 characters")
    private String name;
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;
    @Size(max = 20, message = "Phone number must be at most 20 characters")
    private String phone;
    @Size(max = 200, message = "Website must be at most 200 characters")
    private String website;
    @Size(max = 50, message = "Sponsor type must be at most 50 characters")
    private String sponsorType;
    private Boolean status;
    private LocalDate startDate;
    private LocalDate endDate;
}
