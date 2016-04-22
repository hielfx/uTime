package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.NaturalPerson;
import com.hielfsoft.volunteercrowd.service.NaturalPersonService;
import com.hielfsoft.volunteercrowd.web.rest.util.HeaderUtil;
import com.hielfsoft.volunteercrowd.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing NaturalPerson.
 */
@RestController
@RequestMapping("/api")
public class NaturalPersonResource {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonResource.class);
        
    @Inject
    private NaturalPersonService naturalPersonService;
    
    /**
     * POST  /natural-people : Create a new naturalPerson.
     *
     * @param naturalPerson the naturalPerson to create
     * @return the ResponseEntity with status 201 (Created) and with body the new naturalPerson, or with status 400 (Bad Request) if the naturalPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/natural-people",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> createNaturalPerson(@Valid @RequestBody NaturalPerson naturalPerson) throws URISyntaxException {
        log.debug("REST request to save NaturalPerson : {}", naturalPerson);
        if (naturalPerson.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("naturalPerson", "idexists", "A new naturalPerson cannot already have an ID")).body(null);
        }
        NaturalPerson result = naturalPersonService.save(naturalPerson);
        return ResponseEntity.created(new URI("/api/natural-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("naturalPerson", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /natural-people : Updates an existing naturalPerson.
     *
     * @param naturalPerson the naturalPerson to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated naturalPerson,
     * or with status 400 (Bad Request) if the naturalPerson is not valid,
     * or with status 500 (Internal Server Error) if the naturalPerson couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/natural-people",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> updateNaturalPerson(@Valid @RequestBody NaturalPerson naturalPerson) throws URISyntaxException {
        log.debug("REST request to update NaturalPerson : {}", naturalPerson);
        if (naturalPerson.getId() == null) {
            return createNaturalPerson(naturalPerson);
        }
        NaturalPerson result = naturalPersonService.save(naturalPerson);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("naturalPerson", naturalPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /natural-people : get all the naturalPeople.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of naturalPeople in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/natural-people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NaturalPerson>> getAllNaturalPeople(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NaturalPeople");
        Page<NaturalPerson> page = naturalPersonService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/natural-people");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /natural-people/:id : get the "id" naturalPerson.
     *
     * @param id the id of the naturalPerson to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the naturalPerson, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/natural-people/{id}",
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

    /**
     * DELETE  /natural-people/:id : delete the "id" naturalPerson.
     *
     * @param id the id of the naturalPerson to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/natural-people/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNaturalPerson(@PathVariable Long id) {
        log.debug("REST request to delete NaturalPerson : {}", id);
        naturalPersonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("naturalPerson", id.toString())).build();
    }

    /**
     * SEARCH  /_search/natural-people?query=:query : search for the naturalPerson corresponding
     * to the query.
     *
     * @param query the query of the naturalPerson search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/natural-people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NaturalPerson>> searchNaturalPeople(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NaturalPeople for query {}", query);
        Page<NaturalPerson> page = naturalPersonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/natural-people");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
