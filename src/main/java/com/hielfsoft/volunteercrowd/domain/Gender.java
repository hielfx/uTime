package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Daniel Sánchez on 18/03/2016.
 */
@Entity
@Table(name="gender")
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Gender implements Serializable{

    @Id
    @NotNull
    @Size(min=0,max=50)
    @Column(length=50)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gender gender = (Gender) o;

        return name.equals(gender.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Gender{" +
            "name='" + name + '\'' +
            '}';
    }
}
