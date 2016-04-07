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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
     * @return the persisted entity
     */
    public Availability save(Availability availability) {
        log.debug("Request to save Availability : {}", availability);
        Availability result = availabilityRepository.save(availability);
        availabilitySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the availabilitys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Availability> findAll(Pageable pageable) {
        log.debug("Request to get all Availabilitys");
        Page<Availability> result = availabilityRepository.findAll(pageable);
        return result;
    }

    /**
     *  get one availability by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Availability findOne(Long id) {
        log.debug("Request to get Availability : {}", id);
        Availability availability = availabilityRepository.findOne(id);
        return availability;
    }

    /**
     *  delete the  availability by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Availability : {}", id);
        availabilityRepository.delete(id);
        availabilitySearchRepository.delete(id);
    }

    /**
     * search for the availability corresponding
     * to the query.
     */
    @Transactional(readOnly = true)
    public List<Availability> search(String query) {

        log.debug("REST request to search Availabilitys for query {}", query);
        return StreamSupport
            .stream(availabilitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
