package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.repository.LegalEntityRepository;
import com.hielfsoft.volunteercrowd.repository.search.LegalEntitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
     *
     * @param legalEntity the entity to save
     * @return the persisted entity
     */
    public LegalEntity save(LegalEntity legalEntity) {
        log.debug("Request to save LegalEntity : {}", legalEntity);
        LegalEntity result = legalEntityRepository.save(legalEntity);
        legalEntitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the legalEntities.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LegalEntity> findAll(Pageable pageable) {
        log.debug("Request to get all LegalEntities");
        Page<LegalEntity> result = legalEntityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one legalEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LegalEntity findOne(Long id) {
        log.debug("Request to get LegalEntity : {}", id);
        LegalEntity legalEntity = legalEntityRepository.findOne(id);
        return legalEntity;
    }

    /**
     *  Delete the  legalEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LegalEntity : {}", id);
        legalEntityRepository.delete(id);
        legalEntitySearchRepository.delete(id);
    }

    /**
     * Search for the legalEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LegalEntity> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LegalEntities for query {}", query);
        return legalEntitySearchRepository.search(queryStringQuery(query), pageable);
    }
}
