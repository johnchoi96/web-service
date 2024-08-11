package io.github.johnchoi96.webservice.entities.petfinder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "WEBSERVICE_PETFINDER_PET_LOG")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PetLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PETFINDER_ID", nullable = false, unique = true)
    private Integer petfinderId;

    @ManyToOne
    @JoinColumn(name = "PET_BREED")
    private PetBreedEntity petBreed;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Builder.Default
    @Column(name = "LAST_ACCESSED", nullable = false, length = 200)
    private Instant lastAccessed = Instant.now();

    @Builder.Default
    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt = Instant.now();
}
