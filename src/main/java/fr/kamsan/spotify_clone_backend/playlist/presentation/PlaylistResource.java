package fr.kamsan.spotify_clone_backend.playlist.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.spotify_clone_backend.playlist.application.service.PlaylistService;
import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlist")
public class PlaylistResource {

	private final PlaylistService playlistService;

	@PostMapping("/add-song-to-playlist")
	public ResponseEntity<ReadSongInfoDTO> add(@RequestParam UUID playlistPublicId, @RequestParam UUID songPublicId) {
		ReadSongInfoDTO addSongToPlaylist = playlistService.addSongToPlaylist(playlistPublicId, songPublicId);
		return ResponseEntity.ok(addSongToPlaylist);
	}
}
