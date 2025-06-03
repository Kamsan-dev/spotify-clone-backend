package fr.kamsan.spotify_clone_backend.user.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;

import fr.kamsan.spotify_clone_backend.sharedkernel.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Auth0Service {

	@Value("${okta.oauth2.client-id}")
	private String clientId;

	@Value("${okta.oauth2.client-secret}")
	private String clientSecret;

	@Value("${okta.oauth2.issuer}")
	private String domain;

	public UserInfo getUserInfo(Jwt jwtToken) {
		AuthAPI authAPI = AuthAPI.newBuilder(domain, clientId, clientSecret).build();
		try {
			return authAPI.userInfo(jwtToken.getTokenValue()).execute().getBody();
		} catch (Auth0Exception e) {
			log.error("Failed to fetch user info from Auth0: {}", e.getMessage());
			throw new ApiException("Not possible to fetch user information");
		}
	}

}
