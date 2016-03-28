package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Ability;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ability entity.
 */
public interface AbilityRepository extends JpaRepository<Ability,Long> {

}
