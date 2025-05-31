package fr.kamsan.spotify_clone_backend.playlist.application.dto;

import java.util.UUID;

import fr.kamsan.spotify_clone_backend.playlist.application.dto.sub.PlaylistCoverDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DisplayPlaylistDTO(@Valid PlaylistCoverDTO cover, @NotNull UUID publicId, @NotEmpty String title,
		@NotNull boolean isLikedSongs) {
}
