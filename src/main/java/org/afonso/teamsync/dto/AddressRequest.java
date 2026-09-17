package org.afonso.teamsync.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String street;
    private String city;
    private String district;
    private String postalCode;
    private String country;
}
