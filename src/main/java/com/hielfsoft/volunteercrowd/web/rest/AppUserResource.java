package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.service.AppUserService;
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
 * REST controller for managing AppUser.
 */
@RestController
@RequestMapping("/api")
public class AppUserResource {

    private final Logger log = LoggerFactory.getLogger(AppUserResource.class);
        
    @Inject
    private AppUserService appUserService;
    
    /**
     * POST  /app-users : Create a new appUser.
     *
     * @param appUser the appUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appUser, or with status 400 (Bad Request) if the appUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/app-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppUser> createAppUser(@Valid @RequestBody AppUser appUser) throws URISyntaxException {
        log.debug("REST request to save AppUser : {}", appUser);
        if (appUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("appUser", "idexists", "A new appUser cannot already have an ID")).body(null);
        }
        AppUser result = appUserService.save(appUser);
        return ResponseEntity.created(new URI("/api/app-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("appUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-users : Updates an existing appUser.
     *
     * @param appUser the appUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appUser,
     * or with status 400 (Bad Request) if the appUser is not valid,
     * or with status 500 (Internal Server Error) if the appUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/app-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppUser> updateAppUser(@Valid @RequestBody AppUser appUser) throws URISyntaxException {
        log.debug("REST request to update AppUser : {}", appUser);
        if (appUser.getId() == null) {
            return createAppUser(appUser);
        }
        AppUser result = appUserService.save(appUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("appUser", appUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-users : get all the appUsers.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of appUsers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/app-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AppUser>> getAllAppUsers(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("naturalperson-is-null".equals(filter)) {
            log.debug("REST request to get all AppUsers where naturalPerson is null");
            return new ResponseEntity<>(appUserService.findAllWhereNaturalPersonIsNull(),
                    HttpStatus.OK);
        }
        if ("legalentity-is-null".equals(filter)) {
            log.debug("REST request to get all AppUsers where legalEntity is null");
            return new ResponseEntity<>(appUserService.findAllWhereLegalEntityIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of AppUsers");
        Page<AppUser> page = appUserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/app-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /app-users/:id : get the "id" appUser.
     *
     * @param id the id of the appUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/app-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppUser> getAppUser(@PathVariable Long id) {
        log.debug("REST request to get AppUser : {}", id);
        AppUser appUser = appUserService.findOne(id);
        return Optional.ofNullable(appUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /app-users/:id : delete the "id" appUser.
     *
     * @param id the id of the appUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/app-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        log.debug("REST request to delete AppUser : {}", id);
        appUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appUser", id.toString())).build();
    }

    /**
     * SEARCH  /_search/app-users?query=:query : search for the appUser corresponding
     * to the query.
     *
     * @param query the query of the appUser search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/app-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AppUser>> searchAppUsers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AppUsers for query {}", query);
        Page<AppUser> page = appUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/app-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
