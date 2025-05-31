package fr.kamsan.spotify_clone_backend.playlist.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.spotify_clone_backend.playlist.application.dto.DisplayPlaylistDTO;
import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;
import fr.kamsan.spotify_clone_backend.playlist.domain.embedded.PlaylistSong;
import fr.kamsan.spotify_clone_backend.playlist.domain.embedded.PlaylistSongId;
import fr.kamsan.spotify_clone_backend.playlist.mapper.PlaylistMapper;
import fr.kamsan.spotify_clone_backend.playlist.repository.PlaylistRepository;
import fr.kamsan.spotify_clone_backend.playlist.repository.PlaylistSongRepository;
import fr.kamsan.spotify_clone_backend.sharedkernel.exception.ApiException;
import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import fr.kamsan.spotify_clone_backend.song.domain.Song;
import fr.kamsan.spotify_clone_backend.song.mapper.SongMapper;
import fr.kamsan.spotify_clone_backend.song.repository.SongRepository;
import fr.kamsan.spotify_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.spotify_clone_backend.user.application.service.UserService;
import fr.kamsan.spotify_clone_backend.user.domain.User;
import fr.kamsan.spotify_clone_backend.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {

	private final PlaylistRepository playlistRepository;
	private final PlaylistSongRepository playlistSongRepository;
	private final SongRepository songRepository;
	private final UserService userService;
	private final SongMapper songMapper;
	private final PlaylistMapper playlistMapper;
	private final UserMapper userMapper;

	@Transactional
	public ReadSongInfoDTO addSongToPlaylist(UUID playlistPublicId, UUID songPublicId) {

		Song song = songRepository.findByPublicId(songPublicId).orElseThrow(
				() -> new ApiException(String.format("Cannot retrieve song with public id %s", songPublicId)));

		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();

		Playlist playlist = playlistRepository
				.findByPublicIdAndUser_PublicId(playlistPublicId, connectedUser.publicId())
				.orElseThrow(() -> new ApiException(
						String.format("Cannot retrieve playlist with public id %s", playlistPublicId)));

		PlaylistSong playlistSong = new PlaylistSong();
		PlaylistSongId id = new PlaylistSongId();
		id.setPlaylistId(playlist.getId());
		id.setSongId(song.getId());
		playlistSong.setId(id);
		playlistSong.setPlaylist(playlist);
		playlistSong.setSong(song);

		PlaylistSong savePlaylistSong = playlistSongRepository.save(playlistSong);

		ReadSongInfoDTO songToReadSongInfoDTO = songMapper.songToReadSongInfoDTO(song);
		songToReadSongInfoDTO.setFavorite(true);
		songToReadSongInfoDTO.setPlaylistPublicId(playlistPublicId);
		songToReadSongInfoDTO.setDateAdded(savePlaylistSong.getCreatedAt());
		
		List<UUID> playlistPublicIdsBySongPublicId = playlistSongRepository
				.findPlaylistPublicIdsBySongPublicId(song.getPublicId(), connectedUser.publicId());
		songToReadSongInfoDTO.setPlaylistPublicIds(playlistPublicIdsBySongPublicId);
		
		if (playlistPublicIdsBySongPublicId.size() > 0) {
			songToReadSongInfoDTO.setFavorite(true);
		}

		return songToReadSongInfoDTO;
	}

	@Transactional
	public ReadSongInfoDTO deleteSongFromPlaylist(UUID playlistPublicId, UUID songPublicId) {
		Song song = songRepository.findByPublicId(songPublicId).orElseThrow(
				() -> new ApiException(String.format("Cannot retrieve song with public id %s", songPublicId)));

		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();

		Playlist playlist = playlistRepository
				.findByPublicIdAndUser_PublicId(playlistPublicId, connectedUser.publicId())
				.orElseThrow(() -> new ApiException(
						String.format("Cannot retrieve playlist with public id %s", playlistPublicId)));

		Long deleteSuccess = playlistSongRepository.deleteByPlaylist_publicIdAndSong_publicId(playlistPublicId,
				songPublicId);
		if (deleteSuccess > 0) {
			ReadSongInfoDTO readSongInfoDTO = songMapper.songToReadSongInfoDTO(song);
			List<UUID> playlistPublicIdsBySongPublicId = playlistSongRepository
					.findPlaylistPublicIdsBySongPublicId(song.getPublicId(), connectedUser.publicId());
			readSongInfoDTO.setPlaylistPublicIds(playlistPublicIdsBySongPublicId);
			if (playlistPublicIdsBySongPublicId.size() > 0) {
				readSongInfoDTO.setFavorite(true);
			}
			return readSongInfoDTO;
		} else {
			throw new ApiException(String.format("Unable to delete song from playlist"));
		}
	}

	@Transactional(readOnly = true)
	public List<DisplayPlaylistDTO> getAll() {
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		return playlistRepository.findAllByUserPublicId(connectedUser.publicId()).stream()
				.map(playlistMapper::playlistToDisplayPlaylistDTO).toList();
	}

	public DisplayPlaylistDTO create(String title) {
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		User user = userService.getUserByPublicId(connectedUser.publicId()).orElseThrow(() -> new ApiException(
				String.format("Could not retrieve authenticated user with public id %s", connectedUser.publicId())));
		try {
			Playlist newPlaylist = new Playlist();
			newPlaylist.setTitle(title);
			newPlaylist.setUser(user);
			Playlist savedPlaylist = playlistRepository.save(newPlaylist);

			return playlistMapper.playlistToDisplayPlaylistDTO(savedPlaylist);
		} catch (Exception e) {
			throw new ApiException("Failed to create playlist");
		}
	}
}
