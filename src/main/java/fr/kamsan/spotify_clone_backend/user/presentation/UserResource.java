package fr.kamsan.spotify_clone_backend.user.presentation;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.kamsan.spotify_clone_backend.playlist.application.service.PlaylistService;
import fr.kamsan.spotify_clone_backend.user.application.dto.ReadUserDTO;
import fr.kamsan.spotify_clone_backend.user.application.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class UserResource {

	private final UserService userService;


	@GetMapping("/get-authenticated-user")
	public ResponseEntity<ReadUserDTO> getAuthenticatedUser(@RequestParam boolean forceResync,
			@AuthenticationPrincipal Jwt user) {

		if (user == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			userService.syncWithIdp(user, forceResync);
			ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
			return new ResponseEntity<ReadUserDTO>(connectedUser, HttpStatus.OK);
		}
	}
}
