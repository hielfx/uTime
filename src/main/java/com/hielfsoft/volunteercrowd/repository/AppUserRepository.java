package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.AppUser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AppUser entity.
 */
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

}
