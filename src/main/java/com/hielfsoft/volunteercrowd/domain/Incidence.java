package com.hielfsoft.volunteercrowd.domain;

import com.hielfsoft.volunteercrowd.validator.Past;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Incidence.
 */
@Entity
@Table(name = "incidence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "incidence")
public class Incidence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Past
    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "closed")
    private Boolean closed;

    @NotNull
    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "admin_comment", nullable = false)
    private String adminComment;

    @NotNull
    @ManyToOne
    @Valid
    @JoinColumn(name = "administrator_id", nullable = false)
    private Administrator administrator;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @NotNull
    @Valid
    private Request request;

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

    public Boolean getClosed() {
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
