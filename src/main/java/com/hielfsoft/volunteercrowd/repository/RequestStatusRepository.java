package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Gender;
import com.hielfsoft.volunteercrowd.domain.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Gender entity.
 */
public interface RequestStatusRepository extends JpaRepository<RequestStatus, String> {
}
