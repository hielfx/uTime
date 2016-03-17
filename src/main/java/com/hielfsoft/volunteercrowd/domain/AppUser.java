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
    @Column(name="image")
    private byte[] image;

    private Address address;

    //Relationships
    @OneToOne
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_following_follower",
        joinColumns = {@JoinColumn(name = "app_user", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "ap_user", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<AppUser> followers;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_following_follower",
        joinColumns = {@JoinColumn(name = "app_user", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "ap_user", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<User> following;

    //Getters and Setters

    public List<AppUser> getFollowers() {
        return followers;
    }

    public void setFollowers(List<AppUser> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
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