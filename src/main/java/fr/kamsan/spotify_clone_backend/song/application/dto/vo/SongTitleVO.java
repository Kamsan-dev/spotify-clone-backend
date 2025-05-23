package fr.kamsan.spotify_clone_backend.song.application.dto.vo;

import jakarta.validation.constraints.NotBlank;

public record SongTitleVO(@NotBlank String value) {

}
