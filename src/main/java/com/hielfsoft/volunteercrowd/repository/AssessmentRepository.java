package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Assessment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Assessment entity.
 */
public interface AssessmentRepository extends JpaRepository<Assessment,Long> {

}
