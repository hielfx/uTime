package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Disponibility.
 */
@Entity
@Table(name = "disponibility")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "disponibility")
public class Disponibility implements Serializable {

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
        Disponibility disponibility = (Disponibility) o;
        if(disponibility.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, disponibility.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Disponibility{" +
            "id=" + id +
            ", startMoment='" + startMoment + "'" +
            ", endMoment='" + endMoment + "'" +
            '}';
    }
}
