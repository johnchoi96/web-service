package io.github.johnchoi96.webservice.repos.cfb;

import io.github.johnchoi96.webservice.entities.cfb.CfbWeekSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CfbWeekSummaryRepo extends JpaRepository<CfbWeekSummaryEntity, Long> {

    @Query(value = """
            select entity from CfbWeekSummaryEntity entity
            where entity.week = :WEEK
            and entity.year = :YEAR
            """)
    Optional<CfbWeekSummaryEntity> getCfbWeekSummary(@Param("WEEK") final int week, @Param("YEAR") final int year);
}