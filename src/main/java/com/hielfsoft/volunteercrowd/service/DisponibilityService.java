package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Disponibility;
import com.hielfsoft.volunteercrowd.repository.DisponibilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.DisponibilitySearchRepository;
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
 * Service Implementation for managing Disponibility.
 */
@Service
@Transactional
public class DisponibilityService {

    private final Logger log = LoggerFactory.getLogger(DisponibilityService.class);
    
    @Inject
    private DisponibilityRepository disponibilityRepository;
    
    @Inject
    private DisponibilitySearchRepository disponibilitySearchRepository;
    
    /**
     * Save a disponibility.
     * @return the persisted entity
     */
    public Disponibility save(Disponibility disponibility) {
        log.debug("Request to save Disponibility : {}", disponibility);
        Disponibility result = disponibilityRepository.save(disponibility);
        disponibilitySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the disponibilitys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Disponibility> findAll(Pageable pageable) {
        log.debug("Request to get all Disponibilitys");
        Page<Disponibility> result = disponibilityRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one disponibility by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Disponibility findOne(Long id) {
        log.debug("Request to get Disponibility : {}", id);
        Disponibility disponibility = disponibilityRepository.findOne(id);
        return disponibility;
    }

    /**
     *  delete the  disponibility by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Disponibility : {}", id);
        disponibilityRepository.delete(id);
        disponibilitySearchRepository.delete(id);
    }

    /**
     * search for the disponibility corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Disponibility> search(String query) {
        
        log.debug("REST request to search Disponibilitys for query {}", query);
        return StreamSupport
            .stream(disponibilitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
