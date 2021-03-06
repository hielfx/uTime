package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Address;
import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.domain.Authority;
import com.hielfsoft.volunteercrowd.domain.User;
import com.hielfsoft.volunteercrowd.repository.AppUserRepository;
import com.hielfsoft.volunteercrowd.repository.search.AppUserSearchRepository;
import com.hielfsoft.volunteercrowd.security.SecurityUtils;
import com.hielfsoft.volunteercrowd.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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

    @Inject
    private UserService userService;

    /**
     * Save a appUser.
     *
     * @param appUser the entity to save
     * @return the persisted entity
     */
    public AppUser save(AppUser appUser) {
        log.debug("Request to save AppUser : {}", appUser);

        User user;

        //Set an activation key and save the user
        appUser.getUser().setActivationKey(RandomUtil.generateActivationKey());
        user = userService.save(appUser.getUser());

        //Set the new created user
        appUser.setUser(user);

        AppUser result = appUserRepository.save(appUser);
        appUserSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the appUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AppUser> findAll(Pageable pageable) {
        log.debug("Request to get all AppUsers");
        Page<AppUser> result = appUserRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the appUsers where NaturalPerson is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllWhereNaturalPersonIsNull() {
        log.debug("Request to get all appUsers where NaturalPerson is null");
        return StreamSupport
            .stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getNaturalPerson() == null)
            .collect(Collectors.toList());
    }


    /**
     *  get all the appUsers where LegalEntity is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AppUser> findAllWhereLegalEntityIsNull() {
        log.debug("Request to get all appUsers where LegalEntity is null");
        return StreamSupport
            .stream(appUserRepository.findAll().spliterator(), false)
            .filter(appUser -> appUser.getLegalEntity() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one appUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AppUser findOne(Long id) {
        log.debug("Request to get AppUser : {}", id);
        AppUser appUser = appUserRepository.findOneWithEagerRelationships(id);
        return appUser;
    }

    /**
     *  Delete the  appUser by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AppUser : {}", id);
        appUserRepository.delete(id);
        appUserSearchRepository.delete(id);
    }

    /**
     * Search for the appUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AppUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AppUsers for query {}", query);
        return appUserSearchRepository.search(queryStringQuery(query), pageable);
    }

    public AppUser create() {
        AppUser result;
        Boolean isOnline;
        Integer tokens;
        Address address;

        result = new AppUser();
        isOnline = false;
        tokens = 3;
        address = new Address();

        //This will be changed in the future when the business rule is implemented
        address.setShowAddress(true);
        address.setShowCity(true);
        address.setShowCountry(true);
        address.setShowProvince(true);
        address.setShowZipCode(true);

        result.setIsOnline(isOnline);
        result.setTokens(tokens);
        result.setAddress(address);

        return result;
    }

    public AppUser findOneByPrincipal(){
        return appUserRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin());
    }
}
