package fr.kamsan.spotify_clone_backend.playlist.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import fr.kamsan.spotify_clone_backend.sharedkernel.domain.AbstractAuditingEntity;
import fr.kamsan.spotify_clone_backend.song.domain.Song;
import fr.kamsan.spotify_clone_backend.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Entity
@Table(name = "playlist")
@Getter
@Setter
public class Playlist extends AbstractAuditingEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlistSequenceGenerator")
	@SequenceGenerator(name = "playlistSequenceGenerator", sequenceName = "playlist_generator", allocationSize = 1)
	private Long id;

	@UuidGenerator
	@Column(name = "public_id", nullable = false)
	private UUID publicId;

	@Column(name = "title")
	private String title;
	
	@Column(name="is_liked_songs")
	private boolean isLikedSongs;

//	@UuidGenerator
//	@Column(name = "user_public_id", nullable = false)
//	private UUID userPublicId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_public_id", referencedColumnName = "public_id", nullable = false)
	private User user;
	
	
    @ManyToMany
    @JoinTable(name = "playlist_song",
            joinColumns = {@JoinColumn(name = "playlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")})
    private List<Song> songs = new ArrayList<>();
	

}
