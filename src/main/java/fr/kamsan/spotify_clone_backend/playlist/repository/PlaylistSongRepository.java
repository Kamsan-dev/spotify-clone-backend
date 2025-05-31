package fr.kamsan.spotify_clone_backend.playlist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.kamsan.spotify_clone_backend.playlist.domain.embedded.PlaylistSong;
import fr.kamsan.spotify_clone_backend.playlist.domain.embedded.PlaylistSongId;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId>{
	
	Long deleteByPlaylist_publicIdAndSong_publicId(UUID playlistPublicId, UUID songPublicId);
	
	
	@Query("SELECT p.publicId FROM PlaylistSong ps JOIN ps.playlist p WHERE ps.song.publicId = :songPublicId AND p.user.publicId = :userPublicId")
	List<UUID> findPlaylistPublicIdsBySongPublicId(UUID songPublicId, UUID userPublicId);

}
