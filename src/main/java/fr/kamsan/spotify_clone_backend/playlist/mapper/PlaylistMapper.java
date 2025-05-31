package fr.kamsan.spotify_clone_backend.playlist.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.kamsan.spotify_clone_backend.playlist.application.dto.DisplayPlaylistDTO;
import fr.kamsan.spotify_clone_backend.playlist.domain.Playlist;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
	
	
    @Mapping(target = "cover", ignore = true)
    @Mapping(source = "likedSongs", target = "isLikedSongs")
	DisplayPlaylistDTO playlistToDisplayPlaylistDTO(Playlist playlist);

}
