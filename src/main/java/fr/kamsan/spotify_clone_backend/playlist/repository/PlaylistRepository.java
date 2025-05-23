package fr.kamsan.spotify_clone_backend.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long>{

}
