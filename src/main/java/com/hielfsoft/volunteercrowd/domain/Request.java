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
 * A Request.
 */
@Entity
@Table(name = "request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "request")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    @com.hielfsoft.volunteercrowd.validator.Past
    private ZonedDateTime creationDate;

    @NotNull
    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    @NotBlank
    private String code;

    @NotNull
    @Column(name = "finish_date", nullable = false)
    private ZonedDateTime finishDate;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @NotNull
    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @NotNull
    @Column(name = "modification_date", nullable = false)
    @com.hielfsoft.volunteercrowd.validator.Past
    private ZonedDateTime modificationDate;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(nullable = false)
    private AppUser applicant;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(nullable = false)
    private Need need;

    @OneToOne(mappedBy = "request")
    @JsonIgnore
    @Valid
    private Payment payment;

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Incidence> incidences;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(name="status", nullable = false)
    private RequestStatus requestStatus;

    public Request() {
        incidences = new HashSet<>();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZonedDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(ZonedDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public AppUser getApplicant() {
        return applicant;
    }

    public void setApplicant(AppUser appUser) {
        this.applicant = appUser;
    }

    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Set<Incidence> getIncidences() {
        return incidences;
    }

    public void setIncidences(Set<Incidence> incidences) {
        this.incidences = incidences;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Request request = (Request) o;
        if(request.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, request.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Request{" +
            "id=" + id +
            ", creationDate='" + creationDate + "'" +
            ", description='" + description + "'" +
            ", code='" + code + "'" +
            ", finishDate='" + finishDate + "'" +
            ", deleted='" + deleted + "'" +
            ", paid='" + paid + "'" +
            ", modificationDate='" + modificationDate + "'" +
            '}';
    }
}
