package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appuser")
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "([+]\\d{2})?\\d{9}")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_online")
    private Boolean isOnline;

    @Min(0)
    @Column(name="tokens")
    private int tokens;

    // Data types
    @Lob
    @Column(name="image")
    private byte[] image;

    @AttributeOverrides({
        @AttributeOverride(name = "showAddress", column = @Column(name = "show_address")),
        @AttributeOverride(name = "showCity", column = @Column(name = "show_city")),
        @AttributeOverride(name = "showZipCode", column = @Column(name = "show_zip_code")),
        @AttributeOverride(name = "showCountry", column = @Column(name = "show_country")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "zip_code")),
        @AttributeOverride(name = "showProvince", column = @Column(name = "show_province"))})
    private Address address;

    //Relationships
    @OneToOne
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "following_follower",
        joinColumns = {@JoinColumn(name = "app_user_following_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "app_user_follower_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AppUser> followers = new HashSet<AppUser>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "following_follower",
        joinColumns = {@JoinColumn(name = "app_user_follower_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "app_user_following_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AppUser> following = new HashSet<AppUser>();

    @OneToOne(mappedBy = "appUser")
    @JsonIgnore
    private NaturalPerson naturalPerson;

    @OneToOne(mappedBy = "appUser")
    @JsonIgnore
    private LegalEntity legalEntity;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ability> abilities = new HashSet<Ability>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicant")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Request> requests = new HashSet<Request>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Need> needs = new HashSet<Need>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assessment> createdAssessments = new HashSet<Assessment>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assessment> relatedAssessments = new HashSet<Assessment>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "payer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Payment> payments = new HashSet<Payment>();

    //Getters and Setters


    public Set<Assessment> getCreatedAssessments() {
        return createdAssessments;
    }

    public void setCreatedAssessments(Set<Assessment> createdAssessments) {
        this.createdAssessments = createdAssessments;
    }

    public Set<Assessment> getRelatedAssessments() {
        return relatedAssessments;
    }

    public void setRelatedAssessments(Set<Assessment> relatedAssessments) {
        this.relatedAssessments = relatedAssessments;
    }

    public Set<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(Set<Need> needs) {
        this.needs = needs;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(Set<Ability> abilities) {
        this.abilities = abilities;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public NaturalPerson getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public Set<AppUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<AppUser> followers) {
        this.followers = followers;
    }

    public Set<AppUser> getFollowing() {
        return following;
    }

    public void setFollowing(Set<AppUser> following) {
        this.following = following;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        if(appUser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + id +
            ", phoneNumber='" + phoneNumber + "'" +
            ", isOnline='" + isOnline + "'" +
            '}';
    }
}
