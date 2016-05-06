package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.NaturalPersonForm;
import com.hielfsoft.volunteercrowd.service.NaturalPersonFormService;
import com.hielfsoft.volunteercrowd.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing NaturalPersonForm.
 */
@RestController
@RequestMapping("/api")
public class NaturalPersonFormResource {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonFormResource.class);

    @Inject
    private NaturalPersonFormService naturalPersonFormService;

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
    public ResponseEntity<NaturalPersonForm> createNaturalPersonForm(@Valid @RequestBody NaturalPersonForm naturalPersonForm) throws URISyntaxException {
        log.debug("REST request to save NaturalPersonForm : {}", naturalPersonForm);
        if (naturalPersonForm.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("naturalPersonForm", "idexists", "A new naturalPersonForm cannot already have an ID")).body(null);
        }
        NaturalPersonForm result = naturalPersonFormService.save(naturalPersonForm);
        return ResponseEntity.created(new URI("/api/natural-person-forms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("naturalPersonForm", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /natural-person-forms : Updates an existing naturalPersonForm.
     *
     * @param naturalPersonForm the naturalPersonForm to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated naturalPersonForm,
     * or with status 400 (Bad Request) if the naturalPersonForm is not valid,
     * or with status 500 (Internal Server Error) if the naturalPersonForm couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/natural-person-forms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPersonForm> updateNaturalPersonForm(@Valid @RequestBody NaturalPersonForm naturalPersonForm) throws URISyntaxException {
        log.debug("REST request to update NaturalPersonForm : {}", naturalPersonForm);
        if (naturalPersonForm.getId() == null) {
            return createNaturalPersonForm(naturalPersonForm);
        }
        NaturalPersonForm result = naturalPersonFormService.save(naturalPersonForm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("naturalPersonForm", naturalPersonForm.getId().toString()))
            .body(result);
    }

    /**
     * GET  /natural-person-forms : get all the naturalPersonForms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of naturalPersonForms in body
     */
    @RequestMapping(value = "/natural-person-forms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NaturalPersonForm> getAllNaturalPersonForms() {
        log.debug("REST request to get all NaturalPersonForms");
        return naturalPersonFormService.findAll();
    }

    /**
     * GET  /natural-person-forms/:id : get the "id" naturalPersonForm.
     *
     * @param id the id of the naturalPersonForm to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the naturalPersonForm, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/natural-person-forms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPersonForm> getNaturalPersonForm(@PathVariable Long id) {
        log.debug("REST request to get NaturalPersonForm : {}", id);
        NaturalPersonForm naturalPersonForm = naturalPersonFormService.findOne(id);
        return Optional.ofNullable(naturalPersonForm)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /natural-person-forms/:id : delete the "id" naturalPersonForm.
     *
     * @param id the id of the naturalPersonForm to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/natural-person-forms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNaturalPersonForm(@PathVariable Long id) {
        log.debug("REST request to delete NaturalPersonForm : {}", id);
        naturalPersonFormService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("naturalPersonForm", id.toString())).build();
    }

    /**
     * SEARCH  /_search/natural-person-forms?query=:query : search for the naturalPersonForm corresponding
     * to the query.
     *
     * @param query the query of the naturalPersonForm search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/natural-person-forms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NaturalPersonForm> searchNaturalPersonForms(@RequestParam String query) {
        log.debug("REST request to search NaturalPersonForms for query {}", query);
        return naturalPersonFormService.search(query);
    }

}
