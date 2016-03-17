package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.repository.AppUserRepository;
import com.hielfsoft.volunteercrowd.repository.search.AppUserSearchRepository;
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
 * Service Implementation for managing AppUser.
 */
@Service
@Transactional
public class AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserService.class);
    
    @Inject
    private AppUserRepository appUserRepository;
    
    @Inject
    private AppUserSearchRepository appUserSearchRepository;
    
    /**
     * Save a appUser.
     * @return the persisted entity
     */
    public AppUser save(AppUser appUser) {
        log.debug("Request to save AppUser : {}", appUser);
        AppUser result = appUserRepository.save(appUser);
        appUserSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the appUsers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AppUser> findAll(Pageable pageable) {
        log.debug("Request to get all AppUsers");
        Page<AppUser> result = appUserRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one appUser by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AppUser findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        AppUser appUser = appUserRepository.findOne(id);
        return appUser;
    }

    /**
     *  delete the  appUser by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.delete(id);
        appUserSearchRepository.delete(id);
    }

    /**
     * search for the appUser corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<AppUser> search(String query) {
        
        log.debug("REST request to search AppUsers for query {}", query);
        return StreamSupport
            .stream(appUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
