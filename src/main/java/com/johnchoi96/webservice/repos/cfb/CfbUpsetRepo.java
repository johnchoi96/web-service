package com.johnchoi96.webservice.repos.cfb;

import com.johnchoi96.webservice.entities.cfb.CfbTeamEntity;
import com.johnchoi96.webservice.entities.cfb.CfbUpsetEntity;
import com.johnchoi96.webservice.entities.cfb.CfbWeekSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CfbUpsetRepo extends JpaRepository<CfbUpsetEntity, Long> {

    @Query(value = """
            select entity from CfbUpsetEntity entity
            where entity.cfbWeek.week = :WEEK
            and entity.cfbWeek.year = :YEAR
            """)
    List<CfbUpsetEntity> getUpsetMatches(@Param("WEEK") final int week, @Param("YEAR") final int year);

    @Query(value = """
            select entity from CfbUpsetEntity entity
            where entity.cfbWeek = :CFB_WEEK_ENTITY
            """)
    List<CfbUpsetEntity> getUpsetMatches(@Param("CFB_WEEK_ENTITY") CfbWeekSummaryEntity cfbWeekSummaryEntity);

    @Query(value = """
            select entity from CfbUpsetEntity entity
            where entity.homeTeam = :HOME_TEAM_ENTITY
            and entity.awayTeam = :AWAY_TEAM_ENTITY
            and entity.cfbWeek = :CFB_WEEK_ENTITY
            """)
    CfbUpsetEntity getUpsetMatch(@Param("HOME_TEAM_ENTITY") final CfbTeamEntity homeTeam,
                                 @Param("AWAY_TEAM_ENTITY") final CfbTeamEntity awayTeam,
                                 @Param("CFB_WEEK_ENTITY") final CfbWeekSummaryEntity cfbWeekEntity);
}
