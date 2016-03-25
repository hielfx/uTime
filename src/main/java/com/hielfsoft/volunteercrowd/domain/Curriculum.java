package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.hielfsoft.volunteercrowd.validator.Past;;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Curriculum.
 */
@Entity
@Table(name = "curriculum")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "curriculum")
public class Curriculum implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @URL
    @Column(name = "website")
    private String website;

    @NotNull
    @Past
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @NotNull
    @Past
    @Column(name = "modification_date", nullable = false)
    private ZonedDateTime modificationDate;

    @OneToOne(mappedBy = "curriculum")
    @JsonIgnore
    private NaturalPerson naturalPerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public NaturalPerson getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Curriculum curriculum = (Curriculum) o;
        if(curriculum.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, curriculum.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Curriculum{" +
            "id=" + id +
            ", website='" + website + "'" +
            ", creationDate='" + creationDate + "'" +
            ", modificationDate='" + modificationDate + "'" +
            '}';
    }
}
