package fr.kamsan.spotify_clone_backend.playlist.domain.embedded;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Embeddable
public class PlaylistSongId implements Serializable{
	
	@Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "song_id")
    private Long songId;

}
