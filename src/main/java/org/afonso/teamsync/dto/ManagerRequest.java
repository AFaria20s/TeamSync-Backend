package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ManagerRequest {
    private String name;
    private LocalDate birthDay;
    private String email;
    private String password;
    private String phone;
    private UUID addressId;
}
