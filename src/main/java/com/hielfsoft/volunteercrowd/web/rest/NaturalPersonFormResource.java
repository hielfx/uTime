package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.NaturalPerson;
import com.hielfsoft.volunteercrowd.domain.NaturalPersonForm;
import com.hielfsoft.volunteercrowd.service.MailService;
import com.hielfsoft.volunteercrowd.service.NaturalPersonService;
import com.hielfsoft.volunteercrowd.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing NaturalPersonForm.
 */
@RestController
@RequestMapping("/api")
public class NaturalPersonFormResource {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonFormResource.class);

    @Inject
    private NaturalPersonService naturalPersonService;

    @Inject
    private MailService mailService;

    /**
     * POST  /natural-person-forms : Create a new naturalPersonForm.
     *
     * @param naturalPersonForm the naturalPersonForm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new naturalPersonForm, or with status 400 (Bad Request) if the naturalPersonForm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/natural-person-forms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> createNaturalPerson(@Valid @RequestBody NaturalPersonForm naturalPersonForm, BindingResult binding, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to create and save NaturalPerson : {}", naturalPersonForm);
        if (binding.hasErrors()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("naturalPersonForm", "idexists", "A new naturalPersonForm cannot already have an ID")).body(null);
        } else {
            try {
                NaturalPerson result = naturalPersonService.reconstruct(naturalPersonForm);
                result = naturalPersonService.save(result);
                String baseUrl = request.getScheme() + // "http"
                    "://" +                                // "://"
                    request.getServerName() +              // "myhost"
                    ":" +                                  // ":"
                    request.getServerPort() +              // "80"
                    request.getContextPath();              // "/myContextPath" or "" if deployed in root context

                mailService.sendActivationEmail(result.getAppUser().getUser(), baseUrl);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Throwable oops) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("naturalPersonForm", "idexists", "A new naturalPersonForm cannot already have an ID")).body(null);
            }
        }
    }

    /**
     * GET  /natural-person-forms/:id : get the "id" naturalPerson.
     *
     * @param id the id of the naturalPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the naturalPerson, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/natural-person-forms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> getNaturalPerson(@PathVariable Long id) {
        log.debug("REST request to get NaturalPerson : {}", id);
        NaturalPerson naturalPerson = naturalPersonService.findOne(id);
        return Optional.ofNullable(naturalPerson)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
