package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
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

    @NotNull
    @Column(name = "birth_date", nullable = false)
    @Past
    private ZonedDateTime birthDate;

    @OneToOne
    @NotNull
    @Valid
    @JoinColumn(name = "gender", nullable = false)
    private Gender gender;

    @OneToOne
    @Valid
    @NotNull
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    @OneToOne
    @Valid
//    @JsonBackReference
    @JsonManagedReference
    private Curriculum curriculum;

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

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
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
