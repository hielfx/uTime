package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Curriculum;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Curriculum entity.
 */
public interface CurriculumRepository extends JpaRepository<Curriculum,Long> {

}
