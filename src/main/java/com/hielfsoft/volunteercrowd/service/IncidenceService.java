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
     * 
     * @param incidence the entity to save
     * @return the persisted entity
     */
    public Incidence save(Incidence incidence) {
        log.debug("Request to save Incidence : {}", incidence);
        Incidence result = incidenceRepository.save(incidence);
        incidenceSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the incidences.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Incidence> findAll(Pageable pageable) {
        log.debug("Request to get all Incidences");
        Page<Incidence> result = incidenceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one incidence by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Incidence findOne(Long id) {
        log.debug("Request to get Incidence : {}", id);
        Incidence incidence = incidenceRepository.findOne(id);
        return incidence;
    }

    /**
     *  Delete the  incidence by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Incidence : {}", id);
        incidenceRepository.delete(id);
        incidenceSearchRepository.delete(id);
    }

    /**
     * Search for the incidence corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Incidence> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Incidences for query {}", query);
        return incidenceSearchRepository.search(queryStringQuery(query), pageable);
    }
}
