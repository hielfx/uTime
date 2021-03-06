package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Availability;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Availability entity.
 */
public interface AvailabilityRepository extends JpaRepository<Availability,Long> {

}
