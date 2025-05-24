package fr.kamsan.spotify_clone_backend.song.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.SaveSongDTO;
import fr.kamsan.spotify_clone_backend.song.domain.Song;
import fr.kamsan.spotify_clone_backend.song.domain.SongContent;
import fr.kamsan.spotify_clone_backend.song.mapper.SongContentMapper;
import fr.kamsan.spotify_clone_backend.song.mapper.SongMapper;
import fr.kamsan.spotify_clone_backend.song.repository.SongContentRepository;
import fr.kamsan.spotify_clone_backend.song.repository.SongRepository;
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
		Song savedSong = songRepository.saveAndFlush(newSong);
		
		SongContent songContent = songContentMapper.saveContentDTOToSongContent(saveSongDTO.songContent());
		songContent.setSong(savedSong);
		songContentRepository.save(songContent);
		
		return songMapper.songToReadSongInfoDTO(savedSong);
	}
}
