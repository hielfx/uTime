package com.hielfsoft.volunteercrowd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A NaturalPersonForm.
 */
@Entity
@Table(name = "natural_person_form")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "naturalpersonform")
public class NaturalPersonForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private ZonedDateTime birthDate;

    @NotNull
    @Pattern(regexp = "([+]\\\\d{2})?\\\\d{9}")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "address")
    private String address;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @NotNull
    @Column(name = "province", nullable = false)
    private String province;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "password_confirm", nullable = false)
    private String passwordConfirm;

    @NotNull
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "login", length = 50, nullable = false)
    private String login;

    @NotNull
    @Column(name = "accept_terms_and_conditions", nullable = false)
    private Boolean acceptTermsAndConditions;

    @ManyToOne
    private Gender gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NaturalPersonForm naturalPersonForm = (NaturalPersonForm) o;
        if (naturalPersonForm.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, naturalPersonForm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NaturalPersonForm{" +
            "id=" + id +
            ", birthDate='" + birthDate + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", address='" + address + "'" +
            ", city='" + city + "'" +
            ", zipCode='" + zipCode + "'" +
            ", province='" + province + "'" +
            ", country='" + country + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", password='" + password + "'" +
            ", passwordConfirm='" + passwordConfirm + "'" +
            ", login='" + login + "'" +
            ", acceptTermsAndConditions='" + acceptTermsAndConditions + "'" +
            '}';
    }
}
