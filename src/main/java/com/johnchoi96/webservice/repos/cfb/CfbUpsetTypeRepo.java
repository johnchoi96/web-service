package com.johnchoi96.webservice.repos.cfb;

import com.johnchoi96.webservice.entities.cfb.CfbUpsetTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CfbUpsetTypeRepo extends JpaRepository<CfbUpsetTypeEntity, Long> {

    @Query(value = """
            select entity from CfbUpsetTypeEntity entity
            where entity.upsetType = :UPSET_TYPE
            """)
    Optional<CfbUpsetTypeEntity> getUpsetType(@Param("UPSET_TYPE") final String upsetType);
}
