package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Gender;
import com.hielfsoft.volunteercrowd.repository.GenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Daniel Sánchez on 19/04/2016.
 */
@Service
@Transactional
public class GenderService {

    private final Logger log = LoggerFactory.getLogger(GenderService.class);

    @Inject
    private GenderRepository genderRepository;

    @Transactional(readOnly = true)
    public List<Gender> findAll() {
        log.debug("Request to get all the Genders");
        List<Gender> result = genderRepository.findAll();
        return result;
    }

}
