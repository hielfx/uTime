package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A LegalEntityForm.
 */
public class LegalEntityForm {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Pattern(regexp = "([+]\\d{2})?\\d{9}")
    private String phoneNumber;

    @Lob
    private byte[] image;

    private String imageContentType;

    private String address;

    @NotNull
    private String city;

    @NotNull
    private String zipCode;

    @NotNull
    private String province;

    @NotNull
    private String country;

    @NotNull
    @Size(min = 0, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 0, max = 50)
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String passwordConfirm;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    private String login;

    @NotNull
    private Boolean acceptTermsAndConditions;

    @NotNull
    private String mission;

    @NotNull
    private String vision;

    @URL
    private String website;

    @NotNull
    @NotBlank
    private String description;

    public String getMission() {
        return mission;
    }

    public String getVision() {
        return vision;
    }

    public String getWebsite() {
        return website;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean isAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }

    public void setAcceptTermsAndConditions(Boolean acceptTermsAndConditions) {
        this.acceptTermsAndConditions = acceptTermsAndConditions;
    }
}
