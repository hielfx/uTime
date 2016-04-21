package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Need;
import com.hielfsoft.volunteercrowd.repository.NeedRepository;
import com.hielfsoft.volunteercrowd.repository.search.NeedSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Need.
 */
@Service
@Transactional
public class NeedService {

    private final Logger log = LoggerFactory.getLogger(NeedService.class);

    @Inject
    private NeedRepository needRepository;

    @Inject
    private NeedSearchRepository needSearchRepository;

    /**
     * Save a need.
     *
     * @param need the entity to save
     * @return the persisted entity
     */
    public Need save(Need need) {
        log.debug("Request to save Need : {}", need);
        Need result = needRepository.save(need);
        needSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the needs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Need> findAll(Pageable pageable) {
        log.debug("Request to get all Needs");
        Page<Need> result = needRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one need by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Need findOne(Long id) {
        log.debug("Request to get Need : {}", id);
        Need need = needRepository.findOne(id);
        return need;
    }

    /**
     *  Delete the  need by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Need : {}", id);
        needRepository.delete(id);
        needSearchRepository.delete(id);
    }

    /**
     * Search for the need corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Need> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Needs for query {}", query);
        return needSearchRepository.search(queryStringQuery(query), pageable);
    }
}
