package fr.kamsan.spotify_clone_backend.song.resource;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.kamsan.spotify_clone_backend.sharedkernel.exception.ApiException;
import fr.kamsan.spotify_clone_backend.song.application.dto.ReadSongInfoDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.SaveSongDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongContentDTO;
import fr.kamsan.spotify_clone_backend.song.application.dto.sub.SongCoverDTO;
import fr.kamsan.spotify_clone_backend.song.application.service.SongService;
import fr.kamsan.spotify_clone_backend.user.application.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongResource {

	private final SongService songService;
	private final Validator validator;
	private final UserService userService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ReadSongInfoDTO> add(@RequestPart(name = "cover") MultipartFile cover,
			@RequestPart(name = "file") MultipartFile file, @RequestPart(name = "dto") String saveSongDTOString)
			throws IOException {
		SaveSongDTO saveSongDTO = objectMapper.readValue(saveSongDTOString, SaveSongDTO.class);
		SongContentDTO songContentDTO = mapMultipartFileToSongContentDTO(file);
		SongCoverDTO songCoverDTO = mapMultipartFileToSongCoverDTO(cover);

		saveSongDTO = new SaveSongDTO(saveSongDTO.title(), saveSongDTO.artist(), saveSongDTO.duration(), songContentDTO,
				songCoverDTO);

		Set<ConstraintViolation<SaveSongDTO>> violations = validator.validate(saveSongDTO);
		if (!violations.isEmpty()) {
			String violationsJoined = violations.stream()
					.map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
					.collect(Collectors.joining());
			ProblemDetail validationIssue = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
					"Validation errors for the fields : " + violationsJoined);
			return ResponseEntity.of(validationIssue).build();
		} else {
			return ResponseEntity.ok(songService.saveSong(saveSongDTO));
		}
	}

	@GetMapping("/get-all")
	public ResponseEntity<Page<ReadSongInfoDTO>> getAll(Pageable pageable) {

		return ResponseEntity.ok(songService.getAllSongs(pageable));

	}

	private static SongContentDTO mapMultipartFileToSongContentDTO(MultipartFile file) {
		try {
			return new SongContentDTO(file.getBytes(), file.getContentType());
		} catch (IOException ioe) {
			throw new ApiException(String.format("Cannot parse multipart file: %s", file.getOriginalFilename()));
		}
	}

	private static SongCoverDTO mapMultipartFileToSongCoverDTO(MultipartFile file) {
		try {
			return new SongCoverDTO(file.getBytes(), file.getContentType());
		} catch (IOException ioe) {
			throw new ApiException(String.format("Cannot parse multipart file: %s", file.getOriginalFilename()));
		}
	}

}
