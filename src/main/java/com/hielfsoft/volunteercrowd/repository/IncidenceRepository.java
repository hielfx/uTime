package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Incidence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Incidence entity.
 */
public interface IncidenceRepository extends JpaRepository<Incidence,Long> {

}
