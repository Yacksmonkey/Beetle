package com.example.Beetle.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.serviceRoleKey}")
    private String serviceRoleKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final RestClient restClient = RestClient.create();

    /**
     * Uploads raw bytes to Supabase Storage (public bucket) and returns the public URL.
     */
    public String uploadAvatar(byte[] bytes, String contentType, Long userId) {
        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // Decide extension from content type (minimal)
        String ext = switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> "bin";
        };

        // Path inside bucket (unique)
        String objectPath = "users/" + userId + "/" + UUID.randomUUID() + "." + ext;

        // Supabase Storage upload endpoint:

        String uploadUrl = supabaseUrl.trim() + "/storage/v1/object/" + bucket + "/" + objectPath;

        restClient.post()
                .uri(uploadUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
                .header("apikey", serviceRoleKey)
                .header("x-upsert", "true")
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes)
                .retrieve()
                .toBodilessEntity();

        // Public URL format for public buckets:
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
    }
}
