package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class AthleteRequest {
    private String name;
    private LocalDate birthDay;
    private String email;
    private String password;
    private String phone;
    private String license;
    private String nationality;
}