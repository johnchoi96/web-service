package io.github.johnchoi96.webservice.repos.petfinder;

import io.github.johnchoi96.webservice.entities.petfinder.PetBreedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PetBreedRepo extends JpaRepository<PetBreedEntity, Long> {

    @Query(value = """
            select entity.breed from PetBreedEntity entity
            """)
    Set<String> getBreedList();

    @Query(value = """
            select entity from PetBreedEntity entity where entity.breed = :BREED
            """)
    PetBreedEntity getBreedWithName(@Param("BREED") final String breed);
}
