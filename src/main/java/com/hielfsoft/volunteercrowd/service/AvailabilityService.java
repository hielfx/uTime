package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Availability;
import com.hielfsoft.volunteercrowd.repository.AvailabilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.AvailabilitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Availability.
 */
@Service
@Transactional
public class AvailabilityService {

    private final Logger log = LoggerFactory.getLogger(AvailabilityService.class);

    @Inject
    private AvailabilityRepository availabilityRepository;

    @Inject
    private AvailabilitySearchRepository availabilitySearchRepository;

    /**
     * Save a availability.
     *
     * @param availability the entity to save
     * @return the persisted entity
     */
    public Availability save(Availability availability) {
        log.debug("Request to save Availability : {}", availability);
        Availability result = availabilityRepository.save(availability);
        availabilitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the availabilities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Availability> findAll(Pageable pageable) {
        log.debug("Request to get all Availabilities");
        Page<Availability> result = availabilityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one availability by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Availability findOne(Long id) {
        log.debug("Request to get Availability : {}", id);
        Availability availability = availabilityRepository.findOne(id);
        return availability;
    }

    /**
     *  Delete the  availability by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Availability : {}", id);
        availabilityRepository.delete(id);
        availabilitySearchRepository.delete(id);
    }

    /**
     * Search for the availability corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Availability> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Availabilities for query {}", query);
        return availabilitySearchRepository.search(queryStringQuery(query), pageable);
    }
}
