package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.domain.NaturalPerson;
import com.hielfsoft.volunteercrowd.domain.form.NaturalPersonForm;
import com.hielfsoft.volunteercrowd.domain.User;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonSearchRepository;
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
 * Service Implementation for managing NaturalPerson.
 */
@Service
@Transactional
public class NaturalPersonService {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonService.class);

    @Inject
    private NaturalPersonRepository naturalPersonRepository;

    @Inject
    private NaturalPersonSearchRepository naturalPersonSearchRepository;

    @Inject
    private AppUserService appUserService;

    @Inject
    private UserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;

    /**
     * Save a naturalPerson.
     *
     * @param naturalPerson the entity to save
     * @return the persisted entity
     */
    public NaturalPerson save(NaturalPerson naturalPerson) {
        log.debug("Request to save NaturalPerson : {}", naturalPerson);

        AppUser appUser;

        appUser = appUserService.save(naturalPerson.getAppUser());

        //Set the new created app user
        naturalPerson.setAppUser(appUser);

        NaturalPerson result = naturalPersonRepository.save(naturalPerson);
        naturalPersonSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the naturalPeople.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NaturalPerson> findAll(Pageable pageable) {
        log.debug("Request to get all NaturalPeople");
        Page<NaturalPerson> result = naturalPersonRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one naturalPerson by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public NaturalPerson findOne(Long id) {
        log.debug("Request to get NaturalPerson : {}", id);
        NaturalPerson naturalPerson = naturalPersonRepository.findOne(id);
        return naturalPerson;
    }

    /**
     *  Delete the  naturalPerson by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NaturalPerson : {}", id);
        naturalPersonRepository.delete(id);
        naturalPersonSearchRepository.delete(id);
    }

    /**
     * Search for the naturalPerson corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NaturalPerson> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NaturalPeople for query {}", query);
        return naturalPersonSearchRepository.search(queryStringQuery(query), pageable);
    }

    public NaturalPerson create() {
        NaturalPerson result;
        AppUser appUser;
        User user;

        result = new NaturalPerson();
        appUser = appUserService.create();
        user = userService.create();

        appUser.setUser(user);
        result.setAppUser(appUser);

        return result;
    }

    public NaturalPerson reconstruct(NaturalPersonForm naturalPersonForm) {
        Assert.notNull(naturalPersonForm);
        Assert.isTrue(naturalPersonForm.getAcceptTermsAndConditions(), "You must acept terms and conditions");
        Assert.isTrue(naturalPersonForm.getPassword().equals(naturalPersonForm.getPasswordConfirm()), "The password do not match");

        NaturalPerson result;

        result = create();

        //Natural Person
        result.setBirthDate(naturalPersonForm.getBirthDate());
        result.setGender(naturalPersonForm.getGender());

        //App User
        result.getAppUser().setPhoneNumber(naturalPersonForm.getPhoneNumber());
        result.getAppUser().setImage(naturalPersonForm.getImage());
        result.getAppUser().setImageContentType(naturalPersonForm.getImageContentType());
        //Address
        result.getAppUser().getAddress().setAddress(naturalPersonForm.getAddress());
        result.getAppUser().getAddress().setCity(naturalPersonForm.getCity());
        result.getAppUser().getAddress().setCountry(naturalPersonForm.getCountry());
        result.getAppUser().getAddress().setProvince(naturalPersonForm.getProvince());
        result.getAppUser().getAddress().setZipCode(naturalPersonForm.getZipCode());

        //User
        result.getAppUser().getUser().setFirstName(naturalPersonForm.getFirstName());
        result.getAppUser().getUser().setLastName(naturalPersonForm.getLastName());
        result.getAppUser().getUser().setEmail(naturalPersonForm.getEmail());
        result.getAppUser().getUser().setLogin(naturalPersonForm.getLogin());
        result.getAppUser().getUser().setPassword(naturalPersonForm.getLastName());
        result.getAppUser().getUser().setPassword(passwordEncoder.encode(naturalPersonForm.getPassword()));

        return result;
    }
}
