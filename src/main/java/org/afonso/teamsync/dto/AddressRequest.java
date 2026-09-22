package org.afonso.teamsync.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    @Size(max = 200, message = "Street must be at most 200 characters")
    private String street;
    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;
    @Size(max = 100, message = "District must be at most 100 characters")
    private String district;
    @Size(max = 20, message = "Postal code must be at most 20 characters")
    private String postalCode;
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;
}
