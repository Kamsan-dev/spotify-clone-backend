package fr.kamsan.spotify_clone_backend.song.application.dto.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SongDurationVO(@NotNull @Min(1) @Max(7200) Long value) {

}
