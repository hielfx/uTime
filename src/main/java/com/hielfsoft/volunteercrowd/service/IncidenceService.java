package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Incidence;
import com.hielfsoft.volunteercrowd.repository.IncidenceRepository;
import com.hielfsoft.volunteercrowd.repository.search.IncidenceSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Incidence.
 */
@Service
@Transactional
public class IncidenceService {

    private final Logger log = LoggerFactory.getLogger(IncidenceService.class);
    
    @Inject
    private IncidenceRepository incidenceRepository;
    
    @Inject
    private IncidenceSearchRepository incidenceSearchRepository;
    
    /**
     * Save a incidence.
     * @return the persisted entity
     */
    public Incidence save(Incidence incidence) {
        log.debug("Request to save Incidence : {}", incidence);
        Incidence result = incidenceRepository.save(incidence);
        incidenceSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the incidences.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Incidence> findAll(Pageable pageable) {
        log.debug("Request to get all Incidences");
        Page<Incidence> result = incidenceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one incidence by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Incidence findOne(Long id) {
        log.debug("Request to get Incidence : {}", id);
        Incidence incidence = incidenceRepository.findOne(id);
        return incidence;
    }

    /**
     *  delete the  incidence by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Incidence : {}", id);
        incidenceRepository.delete(id);
        incidenceSearchRepository.delete(id);
    }

    /**
     * search for the incidence corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Incidence> search(String query) {
        
        log.debug("REST request to search Incidences for query {}", query);
        return StreamSupport
            .stream(incidenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
