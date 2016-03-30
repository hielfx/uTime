package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.NeededAbility;
import com.hielfsoft.volunteercrowd.repository.NeededAbilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.NeededAbilitySearchRepository;
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
 * Service Implementation for managing NeededAbility.
 */
@Service
@Transactional
public class NeededAbilityService {

    private final Logger log = LoggerFactory.getLogger(NeededAbilityService.class);
    
    @Inject
    private NeededAbilityRepository neededAbilityRepository;
    
    @Inject
    private NeededAbilitySearchRepository neededAbilitySearchRepository;
    
    /**
     * Save a neededAbility.
     * @return the persisted entity
     */
    public NeededAbility save(NeededAbility neededAbility) {
        log.debug("Request to save NeededAbility : {}", neededAbility);
        NeededAbility result = neededAbilityRepository.save(neededAbility);
        neededAbilitySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the neededAbilitys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<NeededAbility> findAll(Pageable pageable) {
        log.debug("Request to get all NeededAbilitys");
        Page<NeededAbility> result = neededAbilityRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one neededAbility by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public NeededAbility findOne(Long id) {
        log.debug("Request to get NeededAbility : {}", id);
        NeededAbility neededAbility = neededAbilityRepository.findOne(id);
        return neededAbility;
    }

    /**
     *  delete the  neededAbility by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete NeededAbility : {}", id);
        neededAbilityRepository.delete(id);
        neededAbilitySearchRepository.delete(id);
    }

    /**
     * search for the neededAbility corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<NeededAbility> search(String query) {
        
        log.debug("REST request to search NeededAbilitys for query {}", query);
        return StreamSupport
            .stream(neededAbilitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
