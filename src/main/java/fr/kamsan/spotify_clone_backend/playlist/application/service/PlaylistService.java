package fr.kamsan.spotify_clone_backend.playlist.application.service;

import org.springframework.stereotype.Service;

import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;
import fr.kamsan.spotify_clone_backend.playlist.repository.PlaylistRepository;
import fr.kamsan.spotify_clone_backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {
	
	
	private final PlaylistRepository playlistRepository;
	
	public void createLikedSongsPlaylist(User user) {
		Playlist newPlaylist = new Playlist();
		newPlaylist.setTitle("Liked Songs");
		newPlaylist.setUser(user);
		newPlaylist.setLikedSongs(true);
		playlistRepository.save(newPlaylist);
	}

}
