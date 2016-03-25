package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.LegalEntity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LegalEntity entity.
 */
public interface LegalEntityRepository extends JpaRepository<LegalEntity,Long> {

}
