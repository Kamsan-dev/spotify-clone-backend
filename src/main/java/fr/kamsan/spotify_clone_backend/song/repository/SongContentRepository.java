package fr.kamsan.spotify_clone_backend.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.spotify_clone_backend.song.domain.SongContent;

public interface SongContentRepository extends JpaRepository<SongContent, Long>{

}
