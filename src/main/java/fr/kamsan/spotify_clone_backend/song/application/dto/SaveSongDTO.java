package fr.kamsan.spotify_clone_backend.song.application.dto;

import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongContentDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongCoverDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongArtistVO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongDurationVO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongTitleVO;
import jakarta.validation.Valid;

public record SaveSongDTO(

		@Valid SongTitleVO title, @Valid SongArtistVO artist, @Valid SongDurationVO duration,
		@Valid SongContentDTO songContent, @Valid SongCoverDTO cover

) {

}
