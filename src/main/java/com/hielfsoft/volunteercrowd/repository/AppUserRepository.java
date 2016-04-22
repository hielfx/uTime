package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.AppUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the AppUser entity.
 */
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    @Query("select distinct appUser from AppUser appUser left join fetch appUser.followers")
    List<AppUser> findAllWithEagerRelationships();

    @Query("select appUser from AppUser appUser left join fetch appUser.followers where appUser.id =:id")
    AppUser findOneWithEagerRelationships(@Param("id") Long id);

}
