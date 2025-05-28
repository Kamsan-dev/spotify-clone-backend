package fr.kamsan.spotify_clone_backend.song.application.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongCoverDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongArtistVO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongDurationVO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongTitleVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadSongInfoDTO {

	@Valid
	private SongTitleVO title;
	@Valid
	private SongArtistVO artist;
	@Valid
	private SongDurationVO duration;
	@Valid
	private SongCoverDTO cover;
	@NotNull
	private UUID publicId;
	@NotNull
	private boolean isFavorite;
	
    private OffsetDateTime dateAdded;    // nullable if not part of any playlist

    private UUID playlistPublicId;       // nullable if no playlist association

	
}
