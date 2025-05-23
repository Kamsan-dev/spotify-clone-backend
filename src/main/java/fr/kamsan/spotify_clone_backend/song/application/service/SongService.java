package fr.kamsan.spotify_clone_backend.song.application.service;

import org.springframework.stereotype.Service;

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

@Service
@RequiredArgsConstructor
public class SongService {
	
	private final SongRepository songRepository;
	private final SongMapper songMapper;
	private final SongContentMapper songContentMapper;
	private final SongContentRepository songContentRepository;
	private final UserService userService;
	
	
	public ReadSongInfoDTO saveSong(SaveSongDTO saveSongDTO) {
		Song newSong = songMapper.saveSongDTOToSong(saveSongDTO);
		Song savedSong = songRepository.saveAndFlush(newSong);
		
		SongContent saveContent = songContentMapper.saveContentDTOToSongContent(saveSongDTO.songContent());
		saveContent.setSong(savedSong);
		songContentRepository.saveAndFlush(saveContent);
		
		return songMapper.songToReadSongInfoDTO(savedSong);
	}
}
