package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Administrator;
import com.hielfsoft.volunteercrowd.repository.AdministratorRepository;
import com.hielfsoft.volunteercrowd.repository.search.AdministratorSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Administrator.
 */
@Service
@Transactional
public class AdministratorService {

    private final Logger log = LoggerFactory.getLogger(AdministratorService.class);

    @Inject
    private AdministratorRepository administratorRepository;

    @Inject
    private AdministratorSearchRepository administratorSearchRepository;

    /**
     * Save a administrator.
     *
     * @param administrator the entity to save
     * @return the persisted entity
     */
    public Administrator save(Administrator administrator) {
        log.debug("Request to save Administrator : {}", administrator);
        Administrator result = administratorRepository.save(administrator);
        administratorSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the administrators.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Administrator> findAll(Pageable pageable) {
        log.debug("Request to get all Administrators");
        Page<Administrator> result = administratorRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one administrator by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Administrator findOne(Long id) {
        log.debug("Request to get Administrator : {}", id);
        Administrator administrator = administratorRepository.findOne(id);
        return administrator;
    }

    /**
     *  Delete the  administrator by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Administrator : {}", id);
        administratorRepository.delete(id);
        administratorSearchRepository.delete(id);
    }

    /**
     * Search for the administrator corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Administrator> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Administrators for query {}", query);
        return administratorSearchRepository.search(queryStringQuery(query), pageable);
    }
}
