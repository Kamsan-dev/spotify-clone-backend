package fr.kamsan.spotify_clone_backend.playlist.application.dto;

import java.util.List;
import java.util.UUID;

import fr.kamsan.spotify_clone_backend.playlist.application.dto.sub.PlaylistCoverDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayPlaylistDetailsDTO {

	private @Valid PlaylistCoverDTO cover;
	private @NotNull UUID publicId;
	private @NotEmpty String title;
	private @NotNull boolean isLikedSongs;
	private List<ReadSongInfoDTO> songs;

}
