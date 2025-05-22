package fr.kamsan.spotify_clone_backend.user.application.dto;

import java.util.UUID;

public record ReadUserDTO(UUID publicId, String firstName, String lastName, String email, String imageUrl) {

}
