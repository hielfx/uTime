package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Gender;
import com.hielfsoft.volunteercrowd.service.GenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Daniel Sánchez on 19/04/2016.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

    private final Logger log = LoggerFactory.getLogger(GenderResource.class);

    @Inject
    private GenderService genderService;

    @RequestMapping(value = "/genders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Gender>> findAllGenders()
        throws URISyntaxException {
        log.debug("REST request to get a page of Genders");
        List<Gender> genders = genderService.findAll();
        return new ResponseEntity<>(genders, HttpStatus.OK);
    }

    @RequestMapping(value = "/_search/genders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Gender> searchAllGenders()
        throws URISyntaxException {
        log.debug("REST request to get a page of Genders");
        return genderService.findAll();
    }
}
