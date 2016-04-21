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
     * POST  /needed-abilities : Create a new neededAbility.
     *
     * @param neededAbility the neededAbility to create
     * @return the ResponseEntity with status 201 (Created) and with body the new neededAbility, or with status 400 (Bad Request) if the neededAbility has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/needed-abilities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NeededAbility> createNeededAbility(@Valid @RequestBody NeededAbility neededAbility) throws URISyntaxException {
        log.debug("REST request to save NeededAbility : {}", neededAbility);
        if (neededAbility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("neededAbility", "idexists", "A new neededAbility cannot already have an ID")).body(null);
        }
        NeededAbility result = neededAbilityService.save(neededAbility);
        return ResponseEntity.created(new URI("/api/needed-abilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("neededAbility", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /needed-abilities : Updates an existing neededAbility.
     *
     * @param neededAbility the neededAbility to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated neededAbility,
     * or with status 400 (Bad Request) if the neededAbility is not valid,
     * or with status 500 (Internal Server Error) if the neededAbility couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/needed-abilities",
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
     * GET  /needed-abilities : get all the neededAbilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of neededAbilities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/needed-abilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NeededAbility>> getAllNeededAbilities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NeededAbilities");
        Page<NeededAbility> page = neededAbilityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/needed-abilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /needed-abilities/:id : get the "id" neededAbility.
     *
     * @param id the id of the neededAbility to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the neededAbility, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/needed-abilities/{id}",
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
     * DELETE  /needed-abilities/:id : delete the "id" neededAbility.
     *
     * @param id the id of the neededAbility to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/needed-abilities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNeededAbility(@PathVariable Long id) {
        log.debug("REST request to delete NeededAbility : {}", id);
        neededAbilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("neededAbility", id.toString())).build();
    }

    /**
     * SEARCH  /_search/needed-abilities?query=:query : search for the neededAbility corresponding
     * to the query.
     *
     * @param query the query of the neededAbility search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/needed-abilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NeededAbility>> searchNeededAbilities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NeededAbilities for query {}", query);
        Page<NeededAbility> page = neededAbilityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/needed-abilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
