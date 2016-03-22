package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Gender entity.
 */
public interface GenderRepository extends JpaRepository<Gender, String> {
}
