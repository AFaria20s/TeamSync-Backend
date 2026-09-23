package org.afonso.teamsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerProfilePictureRequest {
    @NotBlank(message = "Profile picture URL is required")
    @Size(max = 2048, message = "Profile picture URL must be at most 2048 characters")
    @Pattern(
            regexp = "^https?://\\S+$",
            message = "Profile picture URL must be a valid HTTP or HTTPS URL"
    )
    private String profilePicUrl;
}
