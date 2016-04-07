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
     * POST  /availabilitys -> Create a new availability.
     */
    @RequestMapping(value = "/availabilitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Availability> createAvailability(@Valid @RequestBody Availability availability) throws URISyntaxException {
        log.debug("REST request to save Availability : {}", availability);
        if (availability.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("availability", "idexists", "A new availability cannot already have an ID")).body(null);
        }
        Availability result = availabilityService.save(availability);
        return ResponseEntity.created(new URI("/api/availabilitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("availability", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /availabilitys -> Updates an existing availability.
     */
    @RequestMapping(value = "/availabilitys",
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
     * GET  /availabilitys -> get all the availabilitys.
     */
    @RequestMapping(value = "/availabilitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Availability>> getAllAvailabilitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Availabilitys");
        Page<Availability> page = availabilityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/availabilitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /availabilitys/:id -> get the "id" availability.
     */
    @RequestMapping(value = "/availabilitys/{id}",
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
     * DELETE  /availabilitys/:id -> delete the "id" availability.
     */
    @RequestMapping(value = "/availabilitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        log.debug("REST request to delete Availability : {}", id);
        availabilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("availability", id.toString())).build();
    }

    /**
     * SEARCH  /_search/availabilitys/:query -> search for the availability corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/availabilitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Availability> searchAvailabilitys(@PathVariable String query) {
        log.debug("Request to search Availabilitys for query {}", query);
        return availabilityService.search(query);
    }
}
