package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Need;
import com.hielfsoft.volunteercrowd.service.NeedService;
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
 * REST controller for managing Need.
 */
@RestController
@RequestMapping("/api")
public class NeedResource {

    private final Logger log = LoggerFactory.getLogger(NeedResource.class);

    @Inject
    private NeedService needService;

    /**
     * POST  /needs : Create a new need.
     *
     * @param need the need to create
     * @return the ResponseEntity with status 201 (Created) and with body the new need, or with status 400 (Bad Request) if the need has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/needs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Need> createNeed(@Valid @RequestBody Need need) throws URISyntaxException {
        log.debug("REST request to save Need : {}", need);
        if (need.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("need", "idexists", "A new need cannot already have an ID")).body(null);
        }
        Need result = needService.save(need);
        return ResponseEntity.created(new URI("/api/needs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("need", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /needs : Updates an existing need.
     *
     * @param need the need to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated need,
     * or with status 400 (Bad Request) if the need is not valid,
     * or with status 500 (Internal Server Error) if the need couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/needs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Need> updateNeed(@Valid @RequestBody Need need) throws URISyntaxException {
        log.debug("REST request to update Need : {}", need);
        if (need.getId() == null) {
            return createNeed(need);
        }
        Need result = needService.save(need);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("need", need.getId().toString()))
            .body(result);
    }

    /**
     * GET  /needs : get all the needs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of needs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/needs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Need>> getAllNeeds(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Needs");
        Page<Need> page = needService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/needs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /needs/:id : get the "id" need.
     *
     * @param id the id of the need to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the need, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/needs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Need> getNeed(@PathVariable Long id) {
        log.debug("REST request to get Need : {}", id);
        Need need = needService.findOne(id);
        return Optional.ofNullable(need)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /needs/:id : delete the "id" need.
     *
     * @param id the id of the need to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/needs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNeed(@PathVariable Long id) {
        log.debug("REST request to delete Need : {}", id);
        needService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("need", id.toString())).build();
    }

    /**
     * SEARCH  /_search/needs?query=:query : search for the need corresponding
     * to the query.
     *
     * @param query the query of the need search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/needs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Need>> searchNeeds(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Needs for query {}", query);
        Page<Need> page = needService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/needs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
