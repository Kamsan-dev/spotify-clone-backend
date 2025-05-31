package fr.kamsan.spotify_clone_backend.song.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.kamsan.spotify_clone_backend.song.domain.SongContent;

public interface SongContentRepository extends JpaRepository<SongContent, Long>{
	
	@Query("SELECT sc.id, sc.duration from SongContent sc WHERE sc.id IN :ids")
	List<Object[]> findSongDurationsBySongIds(List<Long> ids);
	
	Optional<SongContent> findOneBySongPublicId(UUID publicId);

}
