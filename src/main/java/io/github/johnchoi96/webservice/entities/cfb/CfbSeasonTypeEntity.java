package io.github.johnchoi96.webservice.entities.cfb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WEBSERVICE_CFB_SEASON_TYPE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CfbSeasonTypeEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SEASON_TYPE", length = 50, nullable = false)
    private String seasonType;
}
