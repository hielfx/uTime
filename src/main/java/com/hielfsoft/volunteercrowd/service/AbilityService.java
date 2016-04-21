package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Ability;
import com.hielfsoft.volunteercrowd.repository.AbilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.AbilitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
     *
     * @param ability the entity to save
     * @return the persisted entity
     */
    public Ability save(Ability ability) {
        log.debug("Request to save Ability : {}", ability);
        Ability result = abilityRepository.save(ability);
        abilitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the abilities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ability> findAll(Pageable pageable) {
        log.debug("Request to get all Abilities");
        Page<Ability> result = abilityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one ability by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Ability findOne(Long id) {
        log.debug("Request to get Ability : {}", id);
        Ability ability = abilityRepository.findOne(id);
        return ability;
    }

    /**
     *  Delete the  ability by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Ability : {}", id);
        abilityRepository.delete(id);
        abilitySearchRepository.delete(id);
    }

    /**
     * Search for the ability corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Ability> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Abilities for query {}", query);
        return abilitySearchRepository.search(queryStringQuery(query), pageable);
    }
}
