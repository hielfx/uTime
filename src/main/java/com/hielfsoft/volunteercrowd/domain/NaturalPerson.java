package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hielfsoft.volunteercrowd.validator.Past;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A NaturalPerson.
 */
@Entity
@Table(name = "natural_person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "naturalperson")
public class NaturalPerson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Past
    @NotNull
    @Column(name = "birth_date", nullable = false)
    private ZonedDateTime birthDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name="gender")
//    @OneToOne
//    @JoinTable(
//        name = "natural_person_gender",
//        joinColumns = {@JoinColumn(name = "natural_person_id", referencedColumnName = "id")},
//        inverseJoinColumns = {@JoinColumn(name = "gender_name", referencedColumnName = "name")})
//    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Gender gender;

    @NotNull
    @OneToOne
    @Valid
    @JoinColumn(name="app_user_id") //Added because the liquibase configuration was using appUser_id
    private AppUser appUser;

    @OneToOne
    @Valid
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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
        NaturalPerson naturalPerson = (NaturalPerson) o;
        if(naturalPerson.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, naturalPerson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NaturalPerson{" +
            "id=" + id +
            ", birthDate='" + birthDate + "'" +
            '}';
    }
}
