package io.github.johnchoi96.webservice.repos;

import io.github.johnchoi96.webservice.entities.AppMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppMetadataRepo extends JpaRepository<AppMetadataEntity, Long> {

    @Query(value = """
            select entity from AppMetadataEntity entity
            where entity.appId = :APP_ID
            """)
    Optional<AppMetadataEntity> getAppMetadataWithAppId(@Param("APP_ID") final String appId);
}
