package fr.kamsan.spotify_clone_backend.song.application.dto;

import java.util.UUID;

import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongCoverDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongArtistVO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongDurationVO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongTitleVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ReadSongInfoDTO(@Valid SongTitleVO title, @Valid SongArtistVO artist, @Valid SongDurationVO duration,
		@Valid SongCoverDTO cover, @NotNull UUID publicId, @NotNull boolean isFavorite) {

}
