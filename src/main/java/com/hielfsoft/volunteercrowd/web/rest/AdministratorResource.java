package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Administrator;
import com.hielfsoft.volunteercrowd.service.AdministratorService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Administrator.
 */
@RestController
@RequestMapping("/api")
public class AdministratorResource {

    private final Logger log = LoggerFactory.getLogger(AdministratorResource.class);
        
    @Inject
    private AdministratorService administratorService;
    
    /**
     * POST  /administrators : Create a new administrator.
     *
     * @param administrator the administrator to create
     * @return the ResponseEntity with status 201 (Created) and with body the new administrator, or with status 400 (Bad Request) if the administrator has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/administrators",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator administrator) throws URISyntaxException {
        log.debug("REST request to save Administrator : {}", administrator);
        if (administrator.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("administrator", "idexists", "A new administrator cannot already have an ID")).body(null);
        }
        Administrator result = administratorService.save(administrator);
        return ResponseEntity.created(new URI("/api/administrators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("administrator", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /administrators : Updates an existing administrator.
     *
     * @param administrator the administrator to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated administrator,
     * or with status 400 (Bad Request) if the administrator is not valid,
     * or with status 500 (Internal Server Error) if the administrator couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/administrators",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Administrator> updateAdministrator(@RequestBody Administrator administrator) throws URISyntaxException {
        log.debug("REST request to update Administrator : {}", administrator);
        if (administrator.getId() == null) {
            return createAdministrator(administrator);
        }
        Administrator result = administratorService.save(administrator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("administrator", administrator.getId().toString()))
            .body(result);
    }

    /**
     * GET  /administrators : get all the administrators.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of administrators in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/administrators",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Administrator>> getAllAdministrators(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Administrators");
        Page<Administrator> page = administratorService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/administrators");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /administrators/:id : get the "id" administrator.
     *
     * @param id the id of the administrator to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the administrator, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/administrators/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Administrator> getAdministrator(@PathVariable Long id) {
        log.debug("REST request to get Administrator : {}", id);
        Administrator administrator = administratorService.findOne(id);
        return Optional.ofNullable(administrator)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /administrators/:id : delete the "id" administrator.
     *
     * @param id the id of the administrator to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/administrators/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        log.debug("REST request to delete Administrator : {}", id);
        administratorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("administrator", id.toString())).build();
    }

    /**
     * SEARCH  /_search/administrators?query=:query : search for the administrator corresponding
     * to the query.
     *
     * @param query the query of the administrator search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/administrators",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Administrator>> searchAdministrators(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Administrators for query {}", query);
        Page<Administrator> page = administratorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/administrators");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
