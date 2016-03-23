package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
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
