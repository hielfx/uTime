package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Incidence;
import com.hielfsoft.volunteercrowd.service.IncidenceService;
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
 * REST controller for managing Incidence.
 */
@RestController
@RequestMapping("/api")
public class IncidenceResource {

    private final Logger log = LoggerFactory.getLogger(IncidenceResource.class);
        
    @Inject
    private IncidenceService incidenceService;
    
    /**
     * POST  /incidences -> Create a new incidence.
     */
    @RequestMapping(value = "/incidences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Incidence> createIncidence(@Valid @RequestBody Incidence incidence) throws URISyntaxException {
        log.debug("REST request to save Incidence : {}", incidence);
        if (incidence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("incidence", "idexists", "A new incidence cannot already have an ID")).body(null);
        }
        Incidence result = incidenceService.save(incidence);
        return ResponseEntity.created(new URI("/api/incidences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("incidence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /incidences -> Updates an existing incidence.
     */
    @RequestMapping(value = "/incidences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Incidence> updateIncidence(@Valid @RequestBody Incidence incidence) throws URISyntaxException {
        log.debug("REST request to update Incidence : {}", incidence);
        if (incidence.getId() == null) {
            return createIncidence(incidence);
        }
        Incidence result = incidenceService.save(incidence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("incidence", incidence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /incidences -> get all the incidences.
     */
    @RequestMapping(value = "/incidences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Incidence>> getAllIncidences(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Incidences");
        Page<Incidence> page = incidenceService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/incidences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /incidences/:id -> get the "id" incidence.
     */
    @RequestMapping(value = "/incidences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Incidence> getIncidence(@PathVariable Long id) {
        log.debug("REST request to get Incidence : {}", id);
        Incidence incidence = incidenceService.findOne(id);
        return Optional.ofNullable(incidence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /incidences/:id -> delete the "id" incidence.
     */
    @RequestMapping(value = "/incidences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIncidence(@PathVariable Long id) {
        log.debug("REST request to delete Incidence : {}", id);
        incidenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("incidence", id.toString())).build();
    }

    /**
     * SEARCH  /_search/incidences/:query -> search for the incidence corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/incidences/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Incidence> searchIncidences(@PathVariable String query) {
        log.debug("Request to search Incidences for query {}", query);
        return incidenceService.search(query);
    }
}
