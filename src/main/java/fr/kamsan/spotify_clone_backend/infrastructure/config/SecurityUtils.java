package fr.kamsan.spotify_clone_backend.infrastructure.config;

import java.util.Map;

import fr.kamsan.spotify_clone_backend.user.domain.User;

public class SecurityUtils {
	
	public static User map0auth2AttributesToUser(Map<String, Object> attributes) {
		User user = new User();
		String username = null;
		String sub = String.valueOf(attributes.get("sub"));
		
        if (attributes.get("preferred_username") != null) {
            username = ((String) attributes.get("preferred_username")).toLowerCase();
        }

        if (attributes.get("given_name") != null) {
            user.setFirstName(((String) attributes.get("given_name")));
        } else if ((attributes.get("nickname") != null)) {
            user.setFirstName(((String) attributes.get("nickname")));
        }

        if (attributes.get("family_name") != null) {
            user.setLastName(((String) attributes.get("family_name")));
        }

        if (attributes.get("email") != null) {
            user.setEmail(((String) attributes.get("email")));
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            user.setEmail(username);
        } else {
            user.setEmail(sub);
        }

        if (attributes.get("picture") != null) {
            user.setImageUrl(((String) attributes.get("picture")));
        }
        
        return user;
	}

}
