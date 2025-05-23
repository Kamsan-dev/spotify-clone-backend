package fr.kamsan.spotify_clone_backend.playlist.application.service;

import org.springframework.stereotype.Service;

import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;
import fr.kamsan.spotify_clone_backend.playlist.repository.PlaylistRepository;
import fr.kamsan.spotify_clone_backend.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistService {
	
	
	private final PlaylistRepository playlistRepository;
	
	public void createLikedSongsPlaylist(User user) {
		Playlist newPlaylist = new Playlist();
		newPlaylist.setTitle("Liked Songs");
		newPlaylist.setUserPublicId(user.getPublicId());
		newPlaylist.setLikedSongs(true);
		playlistRepository.save(newPlaylist);
	}

}
