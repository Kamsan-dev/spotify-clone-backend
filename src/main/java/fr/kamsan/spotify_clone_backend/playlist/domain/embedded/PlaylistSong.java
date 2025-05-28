package fr.kamsan.spotify_clone_backend.playlist.domain.embedded;

import java.time.OffsetDateTime;

import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;
import fr.kamsan.spotify_clone_backend.sharedkernel.domain.AbstractAuditingEntity;
import fr.kamsan.spotify_clone_backend.song.domain.Song;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "playlist_song")
@RequiredArgsConstructor
@Getter
@Setter
public class PlaylistSong extends AbstractAuditingEntity<PlaylistSongId> {
	
	@EmbeddedId
    private PlaylistSongId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

}
