package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A LegalEntity.
 */
@Entity
@Table(name = "legal_entity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "legalentity")
public class LegalEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "mission", nullable = false)
    private String mission;

    @NotNull
    @Column(name = "vision", nullable = false)
    private String vision;

    @NotNull
    @URL
    @Column(name = "website", nullable = false)
    private String website;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @OneToOne
    @Valid
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LegalEntity legalEntity = (LegalEntity) o;
        if(legalEntity.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, legalEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LegalEntity{" +
            "id=" + id +
            ", mission='" + mission + "'" +
            ", vision='" + vision + "'" +
            ", website='" + website + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
