package fr.kamsan.spotify_clone_backend.song.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.spotify_clone_backend.song.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long>{

}
