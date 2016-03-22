package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.NaturalPerson;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NaturalPerson entity.
 */
public interface NaturalPersonRepository extends JpaRepository<NaturalPerson,Long> {

}
