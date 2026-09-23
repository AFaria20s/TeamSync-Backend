package org.afonso.teamsync.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
public class SupabaseStorageService {
    private static final long MAX_PROFILE_PICTURE_SIZE = 5 * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/webp");

    private final RestClient restClient = RestClient.create();

    @Value("${supabase.url:}")
    private String supabaseUrl;

    @Value("${supabase.service-role-key:}")
    private String serviceRoleKey;

    @Value("${supabase.storage.bucket:}")
    private String bucket;

    public String uploadManagerProfilePicture(UUID managerId, MultipartFile file) {
        validateConfiguration();
        validateFile(file);
        String objectPath = "managers/" + managerId + "/profile-picture";
        try {
            restClient
                    .post()
                    .uri(storageObjectUrl(objectPath))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
                    .header("apikey", serviceRoleKey)
                    .header("x-upsert", "true")
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .body(new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return "profile-picture";
                        }
                    })
                    .retrieve()
                    .toBodilessEntity();
        } catch (IOException | RuntimeException exception) {
            throw new IllegalStateException("Unable to upload profile picture", exception);
        }

        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
    }

    public void deleteManagerProfilePicture(UUID managerId) {
        validateConfiguration();
        String objectPath = "managers/" + managerId + "/profile-picture";
        try {
            restClient
                    .delete()
                    .uri(storageObjectUrl(objectPath))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
                    .header("apikey", serviceRoleKey)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RuntimeException exception) {
            throw new IllegalStateException("Unable to delete profile picture", exception);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Profile picture file is required");
        }
        if (file.getSize() > MAX_PROFILE_PICTURE_SIZE) {
            throw new IllegalArgumentException("Profile picture must be at most 5 MB");
        }
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Profile picture must be a JPEG, PNG, or WebP image");
        }
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(supabaseUrl)
                || !StringUtils.hasText(serviceRoleKey)
                || !StringUtils.hasText(bucket)) {
            throw new IllegalStateException("Supabase storage is not configured");
        }
    }

    private String storageObjectUrl(String objectPath) {
        return supabaseUrl + "/storage/v1/object/" + bucket + "/" + objectPath;
    }
}
