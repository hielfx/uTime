package com.hielfsoft.volunteercrowd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appuser")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "([+]\\d{2})?\\d{9}")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @NotNull
    @Min(value = 0)
    @Column(name = "tokens", nullable = false)
    private Integer tokens;

    @Lob
    @Column(name = "image", columnDefinition = "VARBINARY")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Valid
    private Address address;

    @OneToOne(optional = false, cascade = CascadeType.ALL)//Related with User
    @JoinColumn(unique = true, nullable = false)
    @NotNull
//    @Valid
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "app_user_follower",
               joinColumns = @JoinColumn(name="app_users_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="followers_id", referencedColumnName="ID"))
    private Set<AppUser> followers;

    @ManyToMany(mappedBy = "followers")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AppUser> followings;

    @OneToOne(mappedBy = "appUser")
    @JsonIgnore
    @Valid
    private NaturalPerson naturalPerson;

    @OneToOne(mappedBy = "appUser")
    @JsonIgnore
    @Valid
    private LegalEntity legalEntity;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ability> abilities;

    @OneToMany(mappedBy = "applicant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Request> requests;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Need> needs;

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assessment> createdAssessments;

    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assessment> relatedAssessments;

    @OneToMany(mappedBy = "payer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Payment> payments;

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Incidence> incidences;

    public AppUser() {
        followers = new HashSet<>();
        followings = new HashSet<>();
        abilities = new HashSet<>();
        requests = new HashSet<>();
        needs = new HashSet<>();
        createdAssessments = new HashSet<>();
        relatedAssessments = new HashSet<>();
        payments = new HashSet<>();
        incidences = new HashSet<>();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public Boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Integer getTokens() {
        return tokens;
    }

    public void setTokens(Integer tokens) {
        this.tokens = tokens;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AppUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<AppUser> appUsers) {
        this.followers = appUsers;
    }

    public Set<AppUser> getFollowings() {
        return followings;
    }

    public void setFollowings(Set<AppUser> appUsers) {
        this.followings = appUsers;
    }

    public NaturalPerson getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(NaturalPerson naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }

    public Set<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(Set<Ability> abilitys) {
        this.abilities = abilitys;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(Set<Need> needs) {
        this.needs = needs;
    }

    public Set<Assessment> getCreatedAssessments() {
        return createdAssessments;
    }

    public void setCreatedAssessments(Set<Assessment> assessments) {
        this.createdAssessments = assessments;
    }

    public Set<Assessment> getRelatedAssessments() {
        return relatedAssessments;
    }

    public void setRelatedAssessments(Set<Assessment> assessments) {
        this.relatedAssessments = assessments;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Incidence> getIncidences() {
        return incidences;
    }

    public void setIncidences(Set<Incidence> incidences) {
        this.incidences = incidences;
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
            ", tokens='" + tokens + "'" +
            ", image='" + image + "'" +
            ", imageContentType='" + imageContentType + "'" +
            '}';
    }
}
