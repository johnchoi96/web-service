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
@Table(name = "WEBSERVICE_CFB_UPSET_TYPE")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CfbUpsetTypeEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "UPSET_TYPE", length = 100, nullable = false)
    private String upsetType;
}
