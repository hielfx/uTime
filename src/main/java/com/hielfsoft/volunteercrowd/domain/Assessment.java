package com.hielfsoft.volunteercrowd.domain;

import com.hielfsoft.volunteercrowd.validator.Past;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Assessment.
 */
@Entity
@Table(name = "assessment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "assessment")
public class Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "creation_moment", nullable = false)
    @Past
    private ZonedDateTime creationMoment;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    @ManyToOne(optional = false)
    @Valid
    @NotNull
    @JoinColumn(nullable = false)
    private AppUser creator;

    @ManyToOne(optional = false)
    @NotNull
    @Valid
    @JoinColumn(nullable = false)
    private AppUser recipient;

    @OneToOne(optional = false)
    @JoinColumn(unique = true, nullable = false)
    @NotNull
    @Valid
    private Payment payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreationMoment() {
        return creationMoment;
    }

    public void setCreationMoment(ZonedDateTime creationMoment) {
        this.creationMoment = creationMoment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setCreator(AppUser appUser) {
        this.creator = appUser;
    }

    public AppUser getRecipient() {
        return recipient;
    }

    public void setRecipient(AppUser appUser) {
        this.recipient = appUser;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Assessment assessment = (Assessment) o;
        if(assessment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, assessment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Assessment{" +
            "id=" + id +
            ", creationMoment='" + creationMoment + "'" +
            ", rating='" + rating + "'" +
            ", comment='" + comment + "'" +
            '}';
    }
}
