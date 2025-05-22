package fr.kamsan.spotify_clone_backend.sharedkernel.exception;


public class ApiException extends RuntimeException{

	public ApiException(String message) {
        super(message);
    }
}
