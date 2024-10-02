package io.github.johnchoi96.webservice.repos.cfb;

import io.github.johnchoi96.webservice.entities.cfb.CfbSeasonTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CfbSeasonTypeRepo extends JpaRepository<CfbSeasonTypeEntity, Long> {

    @Query(value = """
            select entity from CfbSeasonTypeEntity entity
            where entity.seasonType = :SEASON_TYPE
            """)
    Optional<CfbSeasonTypeEntity> getSeasonType(@Param("SEASON_TYPE") final String seasonType);
}
