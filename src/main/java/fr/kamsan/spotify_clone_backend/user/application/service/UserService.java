package fr.kamsan.spotify_clone_backend.user.application.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.json.auth.UserInfo;

import fr.kamsan.spotify_clone_backend.infrastructure.config.SecurityUtils;
import fr.kamsan.spotify_clone_backend.sharedkernel.exception.ApiException;
import fr.kamsan.spotify_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.spotify_clone_backend.user.domain.User;
import fr.kamsan.spotify_clone_backend.user.mapper.UserMapper;
import fr.kamsan.spotify_clone_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserPlaylistService userPlaylistService;
	private final Auth0Service auth0Service;
	private static final String UPDATED_AT_KEY = "updated_at";
	

	@Transactional(readOnly = true)
	public ReadUserDTO getAuthenticatedUserFromSecurityContext() {
		Jwt principal = (Jwt ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserInfo userInfo = auth0Service.getUserInfo(principal);
		User user = SecurityUtils.map0auth2AttributesToUser(userInfo.getValues());
		Optional<User> userOpt = userRepository.findOneByEmail(user.getEmail());
		return userRepository.findOneByEmail(user.getEmail()).map(userMapper::userToReadUserDTO)
				.orElseThrow(() -> new ApiException("Authenticated user was not found"));
	}
	
	@Transactional
	public void syncWithIdp(Jwt userFromJWT, boolean forceResync) {
		UserInfo userInfo = auth0Service.getUserInfo(userFromJWT);
		Map<String, Object> attributes = userInfo.getValues();
		User user = SecurityUtils.map0auth2AttributesToUser(attributes);
        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
        if (existingUser.isPresent()) {
        	if (attributes.get(UPDATED_AT_KEY) != null) {
        		Instant lastModifiedDate = existingUser.orElseThrow().getLastModifiedDate();
        		Instant idpModifiedDate;
        		if (attributes.get(UPDATED_AT_KEY) instanceof Instant instant) {
        			idpModifiedDate = instant;
        		} else {
        			idpModifiedDate = Instant.parse(attributes.get(UPDATED_AT_KEY).toString());
        		}
        		
        		if (idpModifiedDate.isAfter(lastModifiedDate) || forceResync) {
        			updateUser(user);
        		}
        	}
        } else {
        	User savedUser = userRepository.saveAndFlush(user);
        	userPlaylistService.createLikedSongsPlaylist(savedUser);
        }
	}

	private void updateUser(User user) {
        Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
		if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setImageUrl(user.getImageUrl());
            userRepository.saveAndFlush(userToUpdate);
		}
	}
	
    public Optional<ReadUserDTO> getByPublicId(UUID publicId) {
        Optional<User> oneByPublicId = userRepository.findOneByPublicId(publicId);
        return oneByPublicId.map(userMapper::userToReadUserDTO);
    }
    
	public Optional<ReadUserDTO> getByEmail(String email){
		return userRepository.findOneByEmail(email).map(userMapper::userToReadUserDTO);
	}
	
	public Optional<User> getUserByPublicId(UUID publicId){
		return userRepository.findOneByPublicId(publicId);
	}
}
