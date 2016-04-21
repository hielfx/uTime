package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.NeededAbility;
import com.hielfsoft.volunteercrowd.repository.NeededAbilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.NeededAbilitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
     *
     * @param neededAbility the entity to save
     * @return the persisted entity
     */
    public NeededAbility save(NeededAbility neededAbility) {
        log.debug("Request to save NeededAbility : {}", neededAbility);
        NeededAbility result = neededAbilityRepository.save(neededAbility);
        neededAbilitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the neededAbilities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NeededAbility> findAll(Pageable pageable) {
        log.debug("Request to get all NeededAbilities");
        Page<NeededAbility> result = neededAbilityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one neededAbility by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public NeededAbility findOne(Long id) {
        log.debug("Request to get NeededAbility : {}", id);
        NeededAbility neededAbility = neededAbilityRepository.findOne(id);
        return neededAbility;
    }

    /**
     *  Delete the  neededAbility by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NeededAbility : {}", id);
        neededAbilityRepository.delete(id);
        neededAbilitySearchRepository.delete(id);
    }

    /**
     * Search for the neededAbility corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NeededAbility> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NeededAbilities for query {}", query);
        return neededAbilitySearchRepository.search(queryStringQuery(query), pageable);
    }
}
