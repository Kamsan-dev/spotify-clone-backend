package fr.kamsan.spotify_clone_backend.song.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.kamsan.spotify_clone_backend.song.application.dto.SaveSongDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongContentDTO;
import fr.kamsan.spotify_clone_backend.song.domain.SongContent;

@Mapper(componentModel = "spring")
public interface SongContentMapper {
	
	
    @Mapping(target = "song", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "songId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
	SongContent saveContentDTOToSongContent(SongContentDTO songContent);
    

}
