package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Need.
 */
@Entity
@Table(name = "need")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "need")
public class Need implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    @NotBlank
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @NotNull
    @Column(name = "category", nullable = false)
    @NotBlank
    private String category;

    @Column(name = "deleted",nullable = false)
    @NotNull
    private Boolean deleted;

    @NotNull
    @Column(name = "location", nullable = false)
    @NotBlank
    private String location;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    @com.hielfsoft.volunteercrowd.validator.Past
    private ZonedDateTime creationDate;

    @NotNull
    @Column(name = "modification_date", nullable = false)
    @com.hielfsoft.volunteercrowd.validator.Past
    private ZonedDateTime modificationDate;

    @Column(name = "completed", nullable = false)
    @NotNull
    private Boolean completed;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull
    @Valid
    private AppUser appUser;

    @OneToMany(mappedBy = "need")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NeededAbility> neededAbilities;

    @OneToMany(mappedBy = "need")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Availability> availabilities;

    @OneToMany(mappedBy = "need")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Request> requests;

    public Need() {
        neededAbilities = new HashSet<>();
        availabilities = new HashSet<>();
        requests = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Set<NeededAbility> getNeededAbilities() {
        return neededAbilities;
    }

    public void setNeededAbilities(Set<NeededAbility> neededAbilitys) {
        this.neededAbilities = neededAbilitys;
    }

    public Set<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(Set<Availability> availabilitys) {
        this.availabilities = availabilitys;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Need need = (Need) o;
        if(need.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, need.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Need{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", category='" + category + "'" +
            ", deleted='" + deleted + "'" +
            ", location='" + location + "'" +
            ", creationDate='" + creationDate + "'" +
            ", modificationDate='" + modificationDate + "'" +
            ", completed='" + completed + "'" +
            '}';
    }
}
