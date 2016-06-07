package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Need;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Need entity.
 */
public interface NeedRepository extends JpaRepository<Need,Long> {

    @Query("select n from Need n join n.appUser au where au.id=:appUserId")
    public Collection<Need> findByAppUserId(@Param("appUserId") Long appUserId);

}
