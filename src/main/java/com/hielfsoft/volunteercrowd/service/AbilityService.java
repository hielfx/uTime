package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Ability;
import com.hielfsoft.volunteercrowd.repository.AbilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.AbilitySearchRepository;
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
 * Service Implementation for managing Ability.
 */
@Service
@Transactional
public class AbilityService {

    private final Logger log = LoggerFactory.getLogger(AbilityService.class);
    
    @Inject
    private AbilityRepository abilityRepository;
    
    @Inject
    private AbilitySearchRepository abilitySearchRepository;
    
    /**
     * Save a ability.
     * @return the persisted entity
     */
    public Ability save(Ability ability) {
        log.debug("Request to save Ability : {}", ability);
        Ability result = abilityRepository.save(ability);
        abilitySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the abilitys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Ability> findAll(Pageable pageable) {
        log.debug("Request to get all Abilitys");
        Page<Ability> result = abilityRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one ability by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Ability findOne(Long id) {
        log.debug("Request to get Ability : {}", id);
        Ability ability = abilityRepository.findOne(id);
        return ability;
    }

    /**
     *  delete the  ability by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ability : {}", id);
        abilityRepository.delete(id);
        abilitySearchRepository.delete(id);
    }

    /**
     * search for the ability corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Ability> search(String query) {
        
        log.debug("REST request to search Abilitys for query {}", query);
        return StreamSupport
            .stream(abilitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
