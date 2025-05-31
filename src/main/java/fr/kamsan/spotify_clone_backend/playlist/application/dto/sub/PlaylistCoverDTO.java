package fr.kamsan.spotify_clone_backend.playlist.application.dto.sub;

import jakarta.validation.constraints.NotNull;

public record PlaylistCoverDTO(@NotNull byte[] file, @NotNull String fileContentType) {

}
