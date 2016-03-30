package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Disponibility;
import com.hielfsoft.volunteercrowd.service.DisponibilityService;
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
 * REST controller for managing Disponibility.
 */
@RestController
@RequestMapping("/api")
public class DisponibilityResource {

    private final Logger log = LoggerFactory.getLogger(DisponibilityResource.class);
        
    @Inject
    private DisponibilityService disponibilityService;
    
    /**
     * POST  /disponibilitys -> Create a new disponibility.
     */
    @RequestMapping(value = "/disponibilitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Disponibility> createDisponibility(@Valid @RequestBody Disponibility disponibility) throws URISyntaxException {
        log.debug("REST request to save Disponibility : {}", disponibility);
        if (disponibility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("disponibility", "idexists", "A new disponibility cannot already have an ID")).body(null);
        }
        Disponibility result = disponibilityService.save(disponibility);
        return ResponseEntity.created(new URI("/api/disponibilitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("disponibility", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disponibilitys -> Updates an existing disponibility.
     */
    @RequestMapping(value = "/disponibilitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Disponibility> updateDisponibility(@Valid @RequestBody Disponibility disponibility) throws URISyntaxException {
        log.debug("REST request to update Disponibility : {}", disponibility);
        if (disponibility.getId() == null) {
            return createDisponibility(disponibility);
        }
        Disponibility result = disponibilityService.save(disponibility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("disponibility", disponibility.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disponibilitys -> get all the disponibilitys.
     */
    @RequestMapping(value = "/disponibilitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Disponibility>> getAllDisponibilitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Disponibilitys");
        Page<Disponibility> page = disponibilityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/disponibilitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /disponibilitys/:id -> get the "id" disponibility.
     */
    @RequestMapping(value = "/disponibilitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Disponibility> getDisponibility(@PathVariable Long id) {
        log.debug("REST request to get Disponibility : {}", id);
        Disponibility disponibility = disponibilityService.findOne(id);
        return Optional.ofNullable(disponibility)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /disponibilitys/:id -> delete the "id" disponibility.
     */
    @RequestMapping(value = "/disponibilitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDisponibility(@PathVariable Long id) {
        log.debug("REST request to delete Disponibility : {}", id);
        disponibilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("disponibility", id.toString())).build();
    }

    /**
     * SEARCH  /_search/disponibilitys/:query -> search for the disponibility corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/disponibilitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Disponibility> searchDisponibilitys(@PathVariable String query) {
        log.debug("Request to search Disponibilitys for query {}", query);
        return disponibilityService.search(query);
    }
}
