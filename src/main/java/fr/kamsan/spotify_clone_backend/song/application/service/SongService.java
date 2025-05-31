package fr.kamsan.spotify_clone_backend.song.application.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import fr.kamsan.spotify_clone_backend.sharedkernel.exception.ApiException;
import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.SaveSongDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongContentDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.vo.SongDurationVO;
import fr.kamsan.spotify_clone_backend.song.domain.Song;
import fr.kamsan.spotify_clone_backend.song.domain.SongContent;
import fr.kamsan.spotify_clone_backend.song.mapper.SongContentMapper;
import fr.kamsan.spotify_clone_backend.song.mapper.SongMapper;
import fr.kamsan.spotify_clone_backend.song.repository.SongContentRepository;
import fr.kamsan.spotify_clone_backend.song.repository.SongRepository;
import fr.kamsan.spotify_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.spotify_clone_backend.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SongService {

	private final SongRepository songRepository;
	private final SongMapper songMapper;
	private final SongContentMapper songContentMapper;
	private final SongContentRepository songContentRepository;
	private final UserService userService;

	@Transactional
	public ReadSongInfoDTO saveSong(SaveSongDTO saveSongDTO) {
		Song newSong = songMapper.saveSongDTOToSong(saveSongDTO);
		SongContent songContent = songContentMapper.saveContentDTOToSongContent(saveSongDTO.songContent());

		try {
			Mp3File mp3File = convertByteArrayToMp3File(saveSongDTO.songContent().file());
			if (mp3File.hasId3v2Tag() || mp3File.hasId3v1Tag()) {
				long durationInSeconds = mp3File.getLengthInMilliseconds();
				songContent.setDuration(durationInSeconds);
				newSong.setDuration(durationInSeconds);
			}

		} catch (Exception e) {
			throw new ApiException("Failed to extract MP3 duration from audio track.");
		}

		Song savedSong = songRepository.saveAndFlush(newSong);
		songContent.setSong(savedSong);
		songContentRepository.save(songContent);

		ReadSongInfoDTO readSongInfoDTO = songMapper.songToReadSongInfoDTO(savedSong);
		readSongInfoDTO.setDuration(new SongDurationVO(songContent.getDuration()));

		return readSongInfoDTO;
	}

	@Transactional(readOnly = true)
	public Page<ReadSongInfoDTO> getAllSongs(Pageable pageable) {

		ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
		Page<Song> allSongs = songRepository.findAll(pageable);
		List<Song> songsList = allSongs.getContent();

		List<Long> songsIds = songsList.stream().map(Song::getId).toList();

		// get duration of songs by ids
//		List<Object[]> idAndDurationList = songContentRepository.findSongDurationsBySongIds(songsIds);
//		Map<Long, Long> durationsById = idAndDurationList.stream().collect(Collectors.toMap(arr -> (Long) arr[0], // id
//				arr -> (Long) arr[1] // duration
//		));

		List<Object[]> findPlaylistPublicIdsOfSongUser = songRepository
				.findPlaylistPublicIdsOfSongUser(connectedUser.publicId());
		Map<UUID, List<UUID>> playlistPublicIdsBySongPublicIds = findPlaylistPublicIdsOfSongUser.stream()
				.collect(Collectors.groupingBy(obj -> (UUID) obj[0], // key: songPublicId
						Collectors.mapping(obj -> (UUID) obj[1], // value: playlistPublicId
								Collectors.toList())));

		List<ReadSongInfoDTO> readSongInfoDTOList = songsList.stream().map(song -> {
			ReadSongInfoDTO dto = songMapper.songToReadSongInfoDTO(song);

			dto.setPlaylistPublicIds(
					playlistPublicIdsBySongPublicIds.getOrDefault(song.getPublicId(), Collections.emptyList()));

			// check if songs are part of favorite songs of connected user
			if (playlistPublicIdsBySongPublicIds.containsKey(song.getPublicId())) {
				dto.setFavorite(true);
			}

			return dto;
		}).toList();

		return new PageImpl<>(readSongInfoDTOList, pageable, allSongs.getTotalElements());
	}

	@Transactional(readOnly = true)
	public SongContentDTO getOne(UUID songPublicId) {
		Optional<SongContent> findSongContentBySongPublicId = songContentRepository.findOneBySongPublicId(songPublicId);
		if (findSongContentBySongPublicId.isPresent()) {
			return songContentMapper.songContentToSongContentDTO(findSongContentBySongPublicId.get());
		} else {
			throw new ApiException(String.format("Unable to retrieve song content associated with song of public id %s",
					songPublicId));
		}
	}

	private Set<Long> getUserSongIds(UUID userPublicId) {
		return songRepository.findSongIdsInUserPlaylists(userPublicId);
	}

	private Mp3File convertByteArrayToMp3File(byte[] arr)
			throws IOException, UnsupportedTagException, InvalidDataException {
		byte[] mp3Bytes = arr;

		File tempFile = File.createTempFile("upload", ".mp3");
		tempFile.deleteOnExit();

		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(mp3Bytes);
		}

		Mp3File mp3File = new Mp3File(tempFile);
		return mp3File;
	}
}
