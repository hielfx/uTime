package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.repository.LegalEntityRepository;
import com.hielfsoft.volunteercrowd.repository.search.LegalEntitySearchRepository;
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
 * Service Implementation for managing LegalEntity.
 */
@Service
@Transactional
public class LegalEntityService {

    private final Logger log = LoggerFactory.getLogger(LegalEntityService.class);
    
    @Inject
    private LegalEntityRepository legalEntityRepository;
    
    @Inject
    private LegalEntitySearchRepository legalEntitySearchRepository;
    
    /**
     * Save a legalEntity.
     * @return the persisted entity
     */
    public LegalEntity save(LegalEntity legalEntity) {
        log.debug("Request to save LegalEntity : {}", legalEntity);
        LegalEntity result = legalEntityRepository.save(legalEntity);
        legalEntitySearchRepository.save(result);
        return result;
    }

    /**
     *  get all the legalEntitys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LegalEntity> findAll(Pageable pageable) {
        log.debug("Request to get all LegalEntitys");
        Page<LegalEntity> result = legalEntityRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one legalEntity by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LegalEntity findOne(Long id) {
        log.debug("Request to get LegalEntity : {}", id);
        LegalEntity legalEntity = legalEntityRepository.findOne(id);
        return legalEntity;
    }

    /**
     *  delete the  legalEntity by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete LegalEntity : {}", id);
        legalEntityRepository.delete(id);
        legalEntitySearchRepository.delete(id);
    }

    /**
     * search for the legalEntity corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<LegalEntity> search(String query) {
        
        log.debug("REST request to search LegalEntitys for query {}", query);
        return StreamSupport
            .stream(legalEntitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
