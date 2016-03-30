package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.NeededAbility;
import com.hielfsoft.volunteercrowd.service.NeededAbilityService;
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
 * REST controller for managing NeededAbility.
 */
@RestController
@RequestMapping("/api")
public class NeededAbilityResource {

    private final Logger log = LoggerFactory.getLogger(NeededAbilityResource.class);
        
    @Inject
    private NeededAbilityService neededAbilityService;
    
    /**
     * POST  /neededAbilitys -> Create a new neededAbility.
     */
    @RequestMapping(value = "/neededAbilitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NeededAbility> createNeededAbility(@Valid @RequestBody NeededAbility neededAbility) throws URISyntaxException {
        log.debug("REST request to save NeededAbility : {}", neededAbility);
        if (neededAbility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("neededAbility", "idexists", "A new neededAbility cannot already have an ID")).body(null);
        }
        NeededAbility result = neededAbilityService.save(neededAbility);
        return ResponseEntity.created(new URI("/api/neededAbilitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("neededAbility", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /neededAbilitys -> Updates an existing neededAbility.
     */
    @RequestMapping(value = "/neededAbilitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NeededAbility> updateNeededAbility(@Valid @RequestBody NeededAbility neededAbility) throws URISyntaxException {
        log.debug("REST request to update NeededAbility : {}", neededAbility);
        if (neededAbility.getId() == null) {
            return createNeededAbility(neededAbility);
        }
        NeededAbility result = neededAbilityService.save(neededAbility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("neededAbility", neededAbility.getId().toString()))
            .body(result);
    }

    /**
     * GET  /neededAbilitys -> get all the neededAbilitys.
     */
    @RequestMapping(value = "/neededAbilitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NeededAbility>> getAllNeededAbilitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NeededAbilitys");
        Page<NeededAbility> page = neededAbilityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/neededAbilitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /neededAbilitys/:id -> get the "id" neededAbility.
     */
    @RequestMapping(value = "/neededAbilitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NeededAbility> getNeededAbility(@PathVariable Long id) {
        log.debug("REST request to get NeededAbility : {}", id);
        NeededAbility neededAbility = neededAbilityService.findOne(id);
        return Optional.ofNullable(neededAbility)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /neededAbilitys/:id -> delete the "id" neededAbility.
     */
    @RequestMapping(value = "/neededAbilitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNeededAbility(@PathVariable Long id) {
        log.debug("REST request to delete NeededAbility : {}", id);
        neededAbilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("neededAbility", id.toString())).build();
    }

    /**
     * SEARCH  /_search/neededAbilitys/:query -> search for the neededAbility corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/neededAbilitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NeededAbility> searchNeededAbilitys(@PathVariable String query) {
        log.debug("Request to search NeededAbilitys for query {}", query);
        return neededAbilityService.search(query);
    }
}
