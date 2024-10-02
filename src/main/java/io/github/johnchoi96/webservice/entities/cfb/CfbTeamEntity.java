package io.github.johnchoi96.webservice.entities.cfb;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WEBSERVICE_CFB_TEAM")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CfbTeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEAM_NAME", length = 300, nullable = false)
    private String teamName;
}
