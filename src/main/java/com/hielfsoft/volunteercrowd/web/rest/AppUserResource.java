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
     * POST  /appUsers -> Create a new appUser.
     */
    @RequestMapping(value = "/appUsers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppUser> createAppUser(@Valid @RequestBody AppUser appUser) throws URISyntaxException {
        log.debug("REST request to save AppUser : {}", appUser);
        if (appUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("appUser", "idexists", "A new appUser cannot already have an ID")).body(null);
        }
        AppUser result = appUserService.save(appUser);
        return ResponseEntity.created(new URI("/api/appUsers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("appUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appUsers -> Updates an existing appUser.
     */
    @RequestMapping(value = "/appUsers",
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
     * GET  /appUsers -> get all the appUsers.
     */
    @RequestMapping(value = "/appUsers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AppUser>> getAllAppUsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AppUsers");
        Page<AppUser> page = appUserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appUsers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /appUsers/:id -> get the "id" appUser.
     */
    @RequestMapping(value = "/appUsers/{id}",
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
     * DELETE  /appUsers/:id -> delete the "id" appUser.
     */
    @RequestMapping(value = "/appUsers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAppUser(@PathVariable Long id) {
        log.debug("REST request to delete AppUser : {}", id);
        appUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appUser", id.toString())).build();
    }

    /**
     * SEARCH  /_search/appUsers/:query -> search for the appUser corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/appUsers/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppUser> searchAppUsers(@PathVariable String query) {
        log.debug("Request to search AppUsers for query {}", query);
        return appUserService.search(query);
    }
}
