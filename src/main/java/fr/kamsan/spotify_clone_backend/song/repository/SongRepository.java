package fr.kamsan.spotify_clone_backend.song.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.spotify_clone_backend.song.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long>{
	
	Optional<Song> findByPublicId(UUID publicId);

}
