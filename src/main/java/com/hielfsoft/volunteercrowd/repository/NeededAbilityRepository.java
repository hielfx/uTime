package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.NeededAbility;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NeededAbility entity.
 */
public interface NeededAbilityRepository extends JpaRepository<NeededAbility,Long> {

}
