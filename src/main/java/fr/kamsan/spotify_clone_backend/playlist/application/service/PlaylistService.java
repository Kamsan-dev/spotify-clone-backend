package fr.kamsan.spotify_clone_backend.playlist.application.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.spotify_clone_backend.playlist.application.dto.DisplayPlaylistDTO;
import fr.kamsan.spotify_clone_backend.playlist.application.dto.DisplayPlaylistDetailsDTO;
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

		Long deleteSuccess = playlistSongRepository.deleteByPlaylist_publicIdAndSong_publicId(playlistPublicId,
				songPublicId);
		if (deleteSuccess > 0) {
			ReadSongInfoDTO readSongInfoDTO = songMapper.songToReadSongInfoDTO(song);

			// Retrieve the publicIDs of playlists owned by the connected user that contain
			// the specified song
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

	@Transactional
	public DisplayPlaylistDTO create(String title) {
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		User user = userService.getUserByPublicId(connectedUser.publicId()).orElseThrow(() -> new ApiException(
				String.format("Could not retrieve authenticated user with public id %s", connectedUser.publicId())));
		try {
			Playlist newPlaylist = new Playlist();
			newPlaylist.setTitle(title);
			newPlaylist.setUser(user);
			Playlist savedPlaylist = playlistRepository.saveAndFlush(newPlaylist);
			return playlistMapper.playlistToDisplayPlaylistDTO(savedPlaylist);
		} catch (Exception e) {
			throw new ApiException("Failed to create playlist");
		}
	}

	@Transactional(readOnly = true)
	public DisplayPlaylistDetailsDTO getOne(UUID playlistPublicId) {
		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		Playlist playlist = playlistRepository
				.findByPublicIdAndUser_PublicId(playlistPublicId, connectedUser.publicId())
				.orElseThrow(() -> new ApiException(
						String.format("Cannot retrieve playlist with public id %s", playlistPublicId)));

		List<PlaylistSong> PlaylistSongs = playlistSongRepository.findSongsByPlaylistPublicId(playlistPublicId,
				connectedUser.publicId());

		// get list of playlists where each songs is saved.

		List<Object[]> findPlaylistPublicIdsOfSongUser = songRepository
				.findPlaylistPublicIdsOfSongUser(connectedUser.publicId());
		Map<UUID, List<UUID>> playlistPublicIdsBySongPublicIds = findPlaylistPublicIdsOfSongUser.stream()
				.collect(Collectors.groupingBy(obj -> (UUID) obj[0], // key: songPublicId
						Collectors.mapping(obj -> (UUID) obj[1], // value: playlistPublicId
								Collectors.toList())));

		List<ReadSongInfoDTO> readSongInfoDTOs = PlaylistSongs.stream().map(playlistSong -> {
			ReadSongInfoDTO dto = songMapper.songToReadSongInfoDTO(playlistSong.getSong());
			// list of playlists where song appears.
			dto.setPlaylistPublicIds(
					playlistPublicIdsBySongPublicIds.getOrDefault(playlistSong.getSong().getPublicId(),
					Collections.emptyList()));
			// get add date of song in playlist
			dto.setDateAdded(playlistSong.getCreatedAt());
			dto.setFavorite(true);
			return dto;
		}).toList();

		DisplayPlaylistDetailsDTO playlistDTO = playlistMapper.playlistToDisplayPlaylistDetailsDTO(playlist);
		playlistDTO.setSongs(readSongInfoDTOs);

		return playlistDTO;
	}
}
