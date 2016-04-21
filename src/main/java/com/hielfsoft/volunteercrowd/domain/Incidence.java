package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Incidence.
 */
@Entity
@Table(name = "incidence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "incidence")
public class Incidence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    @com.hielfsoft.volunteercrowd.validator.Past
    private ZonedDateTime creationDate;

    @NotNull
    @Column(name = "closed", nullable = false)
    private Boolean closed;

    @NotNull
    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @Column(name = "admin_comment")
    private String adminComment;

    @ManyToOne
    @Valid
    private Administrator administrator;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(nullable = false)
    private Request request;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(nullable = false)
    private AppUser creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setCreator(AppUser appUser) {
        this.creator = appUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Incidence incidence = (Incidence) o;
        if(incidence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, incidence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Incidence{" +
            "id=" + id +
            ", creationDate='" + creationDate + "'" +
            ", closed='" + closed + "'" +
            ", description='" + description + "'" +
            ", adminComment='" + adminComment + "'" +
            '}';
    }
}
