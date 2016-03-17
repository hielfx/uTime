package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Created by Daniel Sánchez on 17/03/2016.
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class Address {

    @NotNull
    @Column(name = "address")
    private String address;

    @NotNull
    @NotBlank
    @Column(name = "city")
    private String city;

    @NotNull
    @NotBlank
    @Column(name = "zip_code")
    private String zipCode;

    @NotNull
    @NotBlank
    @Column(name = "province")
    private String province;

    @NotNull
    @NotBlank
    @Column(name = "country")
    private String country;

    @Column(name = "show_address")
    private boolean showAddress;

    @Column(name = "show_city")
    private boolean showCity;

    @Column(name = "show_zip_code")
    private boolean showZipCode;

    @Column(name = "show_province")
    private boolean showProvince;

    @Column(name = "show_country")
    private boolean showCountry;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isShowAddress() {
        return showAddress;
    }

    public void setShowAddress(boolean showAddress) {
        this.showAddress = showAddress;
    }

    public boolean isShowCity() {
        return showCity;
    }

    public void setShowCity(boolean showCity) {
        this.showCity = showCity;
    }

    public boolean isShowZipCode() {
        return showZipCode;
    }

    public void setShowZipCode(boolean showZipCode) {
        this.showZipCode = showZipCode;
    }

    public boolean isShowProvince() {
        return showProvince;
    }

    public void setShowProvince(boolean showProvince) {
        this.showProvince = showProvince;
    }

    public boolean isShowCountry() {
        return showCountry;
    }

    public void setShowCountry(boolean showCountry) {
        this.showCountry = showCountry;
    }
}
