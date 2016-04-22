package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.NaturalPerson;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonSearchRepository;
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
 * Service Implementation for managing NaturalPerson.
 */
@Service
@Transactional
public class NaturalPersonService {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonService.class);
    
    @Inject
    private NaturalPersonRepository naturalPersonRepository;
    
    @Inject
    private NaturalPersonSearchRepository naturalPersonSearchRepository;
    
    /**
     * Save a naturalPerson.
     * 
     * @param naturalPerson the entity to save
     * @return the persisted entity
     */
    public NaturalPerson save(NaturalPerson naturalPerson) {
        log.debug("Request to save NaturalPerson : {}", naturalPerson);
        NaturalPerson result = naturalPersonRepository.save(naturalPerson);
        naturalPersonSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the naturalPeople.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<NaturalPerson> findAll(Pageable pageable) {
        log.debug("Request to get all NaturalPeople");
        Page<NaturalPerson> result = naturalPersonRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one naturalPerson by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public NaturalPerson findOne(Long id) {
        log.debug("Request to get NaturalPerson : {}", id);
        NaturalPerson naturalPerson = naturalPersonRepository.findOne(id);
        return naturalPerson;
    }

    /**
     *  Delete the  naturalPerson by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NaturalPerson : {}", id);
        naturalPersonRepository.delete(id);
        naturalPersonSearchRepository.delete(id);
    }

    /**
     * Search for the naturalPerson corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NaturalPerson> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NaturalPeople for query {}", query);
        return naturalPersonSearchRepository.search(queryStringQuery(query), pageable);
    }
}
