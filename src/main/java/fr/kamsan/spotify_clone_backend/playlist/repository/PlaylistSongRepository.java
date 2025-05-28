package fr.kamsan.spotify_clone_backend.playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.kamsan.spotify_clone_backend.playlist.domain.embedded.PlaylistSong;
import fr.kamsan.spotify_clone_backend.playlist.domain.embedded.PlaylistSongId;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId>{
	

}
