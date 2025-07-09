package com.johnchoi96.webservice.entities.petfinder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WEBSERVICE_PETFINDER_PET_BREED")
@Getter
@Setter
public class PetBreedEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "BREED", length = 50, nullable = false)
    private String breed;

    @ManyToOne
    @JoinColumn(name = "TYPE", nullable = false)
    private PetTypeEntity type;
}
