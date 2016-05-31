package com.hielfsoft.volunteercrowd.domain.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Created by Daniel Sánchez on 24/05/2016.
 */
public class NeedForm {

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

    @NotNull
    @Column(name = "location", nullable = false)
    @NotBlank
    private String location;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }
}
