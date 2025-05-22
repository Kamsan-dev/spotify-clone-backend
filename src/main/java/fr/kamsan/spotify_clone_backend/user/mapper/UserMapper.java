package fr.kamsan.spotify_clone_backend.user.mapper;

import org.mapstruct.Mapper;

import fr.kamsan.spotify_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.spotify_clone_backend.user.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	ReadUserDTO userToReadUserDTO(User user);

}
