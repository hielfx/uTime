package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Need;
import com.hielfsoft.volunteercrowd.repository.NeedRepository;
import com.hielfsoft.volunteercrowd.repository.search.NeedSearchRepository;
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
     * @return the persisted entity
     */
    public Need save(Need need) {
        log.debug("Request to save Need : {}", need);
        Need result = needRepository.save(need);
        needSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the needs.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Need> findAll(Pageable pageable) {
        log.debug("Request to get all Needs");
        Page<Need> result = needRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one need by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Need findOne(Long id) {
        log.debug("Request to get Need : {}", id);
        Need need = needRepository.findOne(id);
        return need;
    }

    /**
     *  delete the  need by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Need : {}", id);
        needRepository.delete(id);
        needSearchRepository.delete(id);
    }

    /**
     * search for the need corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Need> search(String query) {
        
        log.debug("REST request to search Needs for query {}", query);
        return StreamSupport
            .stream(needSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
