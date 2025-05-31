package fr.kamsan.spotify_clone_backend.song.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.kamsan.spotify_clone_backend.song.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long> {

	Optional<Song> findByPublicId(UUID publicId);

	@Query("SELECT ps.song.id FROM PlaylistSong ps JOIN ps.playlist p WHERE p.user.publicId = :userPublicId")
	Set<Long> findSongIdsInUserPlaylists(UUID userPublicId);
	
	@Query("SELECT ps.song.publicId, p.publicId FROM PlaylistSong ps JOIN ps.playlist p WHERE p.user.publicId = :userPublicId")
	List<Object[]> findPlaylistPublicIdsOfSongUser(UUID userPublicId);

}
