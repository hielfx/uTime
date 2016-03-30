package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Disponibility;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Disponibility entity.
 */
public interface DisponibilityRepository extends JpaRepository<Disponibility,Long> {

}
