package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Availability;
import com.hielfsoft.volunteercrowd.service.AvailabilityService;
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
 * REST controller for managing Availability.
 */
@RestController
@RequestMapping("/api")
public class AvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(AvailabilityResource.class);
        
    @Inject
    private AvailabilityService availabilityService;
    
    /**
     * POST  /availabilities : Create a new availability.
     *
     * @param availability the availability to create
     * @return the ResponseEntity with status 201 (Created) and with body the new availability, or with status 400 (Bad Request) if the availability has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/availabilities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Availability> createAvailability(@Valid @RequestBody Availability availability) throws URISyntaxException {
        log.debug("REST request to save Availability : {}", availability);
        if (availability.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("availability", "idexists", "A new availability cannot already have an ID")).body(null);
        }
        Availability result = availabilityService.save(availability);
        return ResponseEntity.created(new URI("/api/availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("availability", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /availabilities : Updates an existing availability.
     *
     * @param availability the availability to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated availability,
     * or with status 400 (Bad Request) if the availability is not valid,
     * or with status 500 (Internal Server Error) if the availability couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/availabilities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Availability> updateAvailability(@Valid @RequestBody Availability availability) throws URISyntaxException {
        log.debug("REST request to update Availability : {}", availability);
        if (availability.getId() == null) {
            return createAvailability(availability);
        }
        Availability result = availabilityService.save(availability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("availability", availability.getId().toString()))
            .body(result);
    }

    /**
     * GET  /availabilities : get all the availabilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of availabilities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/availabilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Availability>> getAllAvailabilities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Availabilities");
        Page<Availability> page = availabilityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/availabilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /availabilities/:id : get the "id" availability.
     *
     * @param id the id of the availability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the availability, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/availabilities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Availability> getAvailability(@PathVariable Long id) {
        log.debug("REST request to get Availability : {}", id);
        Availability availability = availabilityService.findOne(id);
        return Optional.ofNullable(availability)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /availabilities/:id : delete the "id" availability.
     *
     * @param id the id of the availability to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/availabilities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        log.debug("REST request to delete Availability : {}", id);
        availabilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("availability", id.toString())).build();
    }

    /**
     * SEARCH  /_search/availabilities?query=:query : search for the availability corresponding
     * to the query.
     *
     * @param query the query of the availability search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/availabilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Availability>> searchAvailabilities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Availabilities for query {}", query);
        Page<Availability> page = availabilityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/availabilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
