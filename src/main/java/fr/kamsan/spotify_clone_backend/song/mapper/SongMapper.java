package fr.kamsan.spotify_clone_backend.song.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.SaveSongDTO;
import fr.kamsan.spotify_clone_backend.song.domain.Song;

@Mapper(componentModel = "spring")
public interface SongMapper {
	
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(source = "cover.fileContentType", target = "coverContentType")
    @Mapping(source = "cover.file", target = "cover")
    @Mapping(source = "duration.value", target = "duration")
    @Mapping(source = "title.value", target = "title")
    @Mapping(source = "artist.value", target = "artistName")
	Song saveSongDTOToSong(SaveSongDTO saveSongDTO);
    
    
    
    @Mapping(target = "isFavorite", ignore = true)
    @Mapping(source = "artistName", target = "artist.value")
    @Mapping(source = "title", target = "title.value")
    @Mapping(source = "duration", target = "duration.value")
    @Mapping(source = "cover", target = "cover.file")
    @Mapping(source = "coverContentType", target="cover.fileContentType")
    @Mapping(source = "publicId", target="publicId")
    ReadSongInfoDTO songToReadSongInfoDTO(Song song);

}
