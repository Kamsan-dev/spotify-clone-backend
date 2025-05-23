package fr.kamsan.spotify_clone_backend.song.application.dto.sub;

import jakarta.validation.constraints.NotNull;

public record SongCoverDTO(@NotNull byte[] file, @NotNull String fileContentType) {

}
