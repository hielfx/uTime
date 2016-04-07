package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Availability.
 */
@Entity
@Table(name = "availability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "availability")
public class Availability implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "start_moment", nullable = false)
    private ZonedDateTime startMoment;

    @NotNull
    @Column(name = "end_moment", nullable = false)
    private ZonedDateTime endMoment;

    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "need_id")
    private Need need;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartMoment() {
        return startMoment;
    }

    public void setStartMoment(ZonedDateTime startMoment) {
        this.startMoment = startMoment;
    }

    public ZonedDateTime getEndMoment() {
        return endMoment;
    }

    public void setEndMoment(ZonedDateTime endMoment) {
        this.endMoment = endMoment;
    }

    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Availability availability = (Availability) o;
        if (availability.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, availability.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Availability{" +
            "id=" + id +
            ", startMoment='" + startMoment + "'" +
            ", endMoment='" + endMoment + "'" +
            '}';
    }
}
