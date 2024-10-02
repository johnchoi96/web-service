package io.github.johnchoi96.webservice.repos.cfb;

import io.github.johnchoi96.webservice.entities.cfb.CfbTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CfbTeamRepo extends JpaRepository<CfbTeamEntity, Long> {

    @Query(value = """
            select entity from CfbTeamEntity entity
            where entity.teamName = :TEAM_NAME
            """)
    Optional<CfbTeamEntity> getTeamWithName(@Param("TEAM_NAME") final String teamName);
}
