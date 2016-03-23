package com.hielfsoft.volunteercrowd.repository;

import com.hielfsoft.volunteercrowd.domain.Request;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Request entity.
 */
public interface RequestRepository extends JpaRepository<Request,Long> {

}
