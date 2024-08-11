package io.github.johnchoi96.webservice.repos.petfinder;

import io.github.johnchoi96.webservice.entities.petfinder.PetLogEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PetLogRepo extends JpaRepository<PetLogEntity, Long> {

    @Query(value = """
            select entity from PetLogEntity entity
            where entity.petfinderId = :PETFINDER_ID
            """)
    Optional<PetLogEntity> getPetLogWithPetfinderId(@Param("PETFINDER_ID") final Integer petfinderId);

    @Transactional
    @Modifying
    @Query(value = """
            delete from PetLogEntity entity
            where entity.lastAccessed < :DATE
            """)
    void deleteLogOlderThanMonths(@Param("DATE") final Instant months);
}
