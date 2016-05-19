package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.domain.LegalEntityForm;
import com.hielfsoft.volunteercrowd.service.LegalEntityService;
import com.hielfsoft.volunteercrowd.service.MailService;
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
 * REST controller for managing LegalEntityForm.
 */
@RestController
@RequestMapping("/api")
public class LegalEntityFormResource {

    private final Logger log = LoggerFactory.getLogger(LegalEntityFormResource.class);

    @Inject
    private LegalEntityService legalEntityService;

    @Inject
    private MailService mailService;

    /**
     * POST  /legal-entity-forms : Create a new legalEntityForm.
     *
     * @param legalEntityForm the legalEntityForm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new legalEntityForm, or with status 400 (Bad Request) if the legalEntityForm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/legal-entity-forms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> createLegalEntity(@Valid @RequestBody LegalEntityForm legalEntityForm, BindingResult binding, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to create and save LegalEntity : {}", legalEntityForm);
        if (binding.hasErrors()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("legalEntityForm", "idexists", "A new legalEntityForm cannot already have an ID")).body(null);
        } else {
            try {
                LegalEntity result = legalEntityService.reconstruct(legalEntityForm);
                result = legalEntityService.save(result);
                String baseUrl = request.getScheme() + // "http"
                    "://" +                                // "://"
                    request.getServerName() +              // "myhost"
                    ":" +                                  // ":"
                    request.getServerPort() +              // "80"
                    request.getContextPath();              // "/myContextPath" or "" if deployed in root context

                mailService.sendActivationEmail(result.getAppUser().getUser(), baseUrl);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Throwable oops) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("legalEntityForm", "idexists", "A new legalEntityForm cannot already have an ID")).body(null);
            }
        }
    }

    /**
     * GET  /legal-entity-forms/:id : get the "id" legalEntity.
     *
     * @param id the id of the legalEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the legalEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/legal-entity-forms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> getLegalEntity(@PathVariable Long id) {
        log.debug("REST request to get LegalEntity : {}", id);
        LegalEntity legalEntity = legalEntityService.findOne(id);
        return Optional.ofNullable(legalEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
