package fr.kamsan.spotify_clone_backend.song.domain;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import fr.kamsan.spotify_clone_backend.sharedkernel.domain.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="song")
@RequiredArgsConstructor
@Getter
@Setter
public class Song extends AbstractAuditingEntity<Long>{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "songSequenceGenerator")
	@SequenceGenerator(name = "songSequenceGenerator", sequenceName = "song_generator", allocationSize = 1)
	private Long id;
	
    @UuidGenerator
	@Column(name = "public_id", nullable = false)
	private UUID publicId;
    
	@Column(name = "title", nullable=false)
	private String title;
	
	@Column(name = "artist_name", nullable=false)
	private String artistName;
	
    @Lob
    @Column(name = "cover", nullable = false)
    private byte[] cover;

    @Column(name = "cover_content_type", nullable = false)
    private String coverContentType;
	
	

}
