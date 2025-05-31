package fr.kamsan.spotify_clone_backend.playlist.presentation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.spotify_clone_backend.playlist.application.dto.DisplayPlaylistDTO;
import fr.kamsan.spotify_clone_backend.playlist.application.service.PlaylistService;
import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import jakarta.validation.constraints.NotNull;
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

	@GetMapping("/get-all")
	public ResponseEntity<List<DisplayPlaylistDTO>> getAll() {
		return ResponseEntity.ok(playlistService.getAll());
	}

	@PostMapping("/create")
	public ResponseEntity<DisplayPlaylistDTO> create(@RequestParam @NotNull String title) {
		return ResponseEntity.ok(playlistService.create(title));
	}

	@DeleteMapping("/delete-song-from-playlist")
	public ResponseEntity<ReadSongInfoDTO> delete(@RequestParam UUID playlistPublicId,
			@RequestParam UUID songPublicId) {
		ReadSongInfoDTO deleteSongFromPlaylist = playlistService.deleteSongFromPlaylist(playlistPublicId, songPublicId);
		return ResponseEntity.ok(deleteSongFromPlaylist);
	}
}
