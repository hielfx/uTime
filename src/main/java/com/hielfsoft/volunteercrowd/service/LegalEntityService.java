package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.domain.LegalEntityForm;
import com.hielfsoft.volunteercrowd.domain.User;
import com.hielfsoft.volunteercrowd.repository.LegalEntityRepository;
import com.hielfsoft.volunteercrowd.repository.search.LegalEntitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

    @Inject
    private AppUserService appUserService;

    @Inject
    private UserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;

    /**
     * Save a legalEntity.
     *
     * @param legalEntity the entity to save
     * @return the persisted entity
     */
    public LegalEntity save(LegalEntity legalEntity) {
        log.debug("Request to save LegalEntity : {}", legalEntity);

        AppUser appUser;

        appUser = appUserService.save(legalEntity.getAppUser());

        //Set the new created app user
        legalEntity.setAppUser(appUser);


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

    public LegalEntity create() {
        LegalEntity result;
        AppUser appUser;
        User user;

        result = new LegalEntity();
        appUser = appUserService.create();
        user = userService.create();

        appUser.setUser(user);
        result.setAppUser(appUser);

        return result;
    }

    public LegalEntity reconstruct(LegalEntityForm legalEntityForm) {
        Assert.notNull(legalEntityForm);
        Assert.isTrue(legalEntityForm.getAcceptTermsAndConditions(), "You must acept terms and conditions");
        Assert.isTrue(legalEntityForm.getPassword().equals(legalEntityForm.getPasswordConfirm()), "The password do not match");

        LegalEntity result;

        result = create();

        //Legal Entity
        result.setMission(legalEntityForm.getMission());
        result.setDescription(legalEntityForm.getDescription());
        result.setVision(legalEntityForm.getVision());
        result.setWebsite(legalEntityForm.getWebsite());

        //App User
        result.getAppUser().setPhoneNumber(legalEntityForm.getPhoneNumber());
        result.getAppUser().setImage(legalEntityForm.getImage());
        result.getAppUser().setImageContentType(legalEntityForm.getImageContentType());
        //Address
        result.getAppUser().getAddress().setAddress(legalEntityForm.getAddress());
        result.getAppUser().getAddress().setCity(legalEntityForm.getCity());
        result.getAppUser().getAddress().setCountry(legalEntityForm.getCountry());
        result.getAppUser().getAddress().setProvince(legalEntityForm.getProvince());
        result.getAppUser().getAddress().setZipCode(legalEntityForm.getZipCode());

        //User
        result.getAppUser().getUser().setFirstName(legalEntityForm.getFirstName());
        result.getAppUser().getUser().setLastName(legalEntityForm.getLastName());
        result.getAppUser().getUser().setEmail(legalEntityForm.getEmail());
        result.getAppUser().getUser().setLogin(legalEntityForm.getLogin());
        result.getAppUser().getUser().setPassword(legalEntityForm.getLastName());
        result.getAppUser().getUser().setPassword(passwordEncoder.encode(legalEntityForm.getPassword()));

        return result;
    }


}
