package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Administrator;
import com.hielfsoft.volunteercrowd.repository.AdministratorRepository;
import com.hielfsoft.volunteercrowd.repository.search.AdministratorSearchRepository;
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
     * @return the persisted entity
     */
    public Administrator save(Administrator administrator) {
        log.debug("Request to save Administrator : {}", administrator);
        Administrator result = administratorRepository.save(administrator);
        administratorSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the administrators.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Administrator> findAll(Pageable pageable) {
        log.debug("Request to get all Administrators");
        Page<Administrator> result = administratorRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one administrator by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Administrator findOne(Long id) {
        log.debug("Request to get Administrator : {}", id);
        Administrator administrator = administratorRepository.findOne(id);
        return administrator;
    }

    /**
     *  delete the  administrator by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Administrator : {}", id);
        administratorRepository.delete(id);
        administratorSearchRepository.delete(id);
    }

    /**
     * search for the administrator corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Administrator> search(String query) {
        
        log.debug("REST request to search Administrators for query {}", query);
        return StreamSupport
            .stream(administratorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
