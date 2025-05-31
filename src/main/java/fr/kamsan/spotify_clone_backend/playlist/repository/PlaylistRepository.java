package fr.kamsan.spotify_clone_backend.playlist.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long>{
	
	Optional<Playlist> findByPublicIdAndUser_PublicId(UUID publicId, UUID userPublicId);
	
	List<Playlist> findAllByUserPublicId(UUID publicId);

}
