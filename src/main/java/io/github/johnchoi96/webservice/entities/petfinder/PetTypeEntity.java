package io.github.johnchoi96.webservice.entities.petfinder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WEBSERVICE_PETFINDER_PET_TYPE")
@Getter
@Setter
public class PetTypeEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE", nullable = false, unique = true, length = 100)
    private String type;
}
