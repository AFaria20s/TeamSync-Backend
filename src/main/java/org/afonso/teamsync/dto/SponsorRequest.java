package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class SponsorRequest {
    private UUID addressId;
    private String name;
    private String email;
    private String phone;
    private String website;
    private String sponsorType;
    private Boolean status;
    private LocalDate startDate;
    private LocalDate endDate;
}
