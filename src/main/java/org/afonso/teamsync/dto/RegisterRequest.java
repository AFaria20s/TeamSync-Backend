package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String managerName;
    private String email;
    private String password;
    private String teamName;
}
