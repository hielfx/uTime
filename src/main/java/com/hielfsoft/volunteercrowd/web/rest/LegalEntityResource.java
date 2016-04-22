package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.service.LegalEntityService;
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
 * REST controller for managing LegalEntity.
 */
@RestController
@RequestMapping("/api")
public class LegalEntityResource {

    private final Logger log = LoggerFactory.getLogger(LegalEntityResource.class);
        
    @Inject
    private LegalEntityService legalEntityService;
    
    /**
     * POST  /legal-entities : Create a new legalEntity.
     *
     * @param legalEntity the legalEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new legalEntity, or with status 400 (Bad Request) if the legalEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/legal-entities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> createLegalEntity(@Valid @RequestBody LegalEntity legalEntity) throws URISyntaxException {
        log.debug("REST request to save LegalEntity : {}", legalEntity);
        if (legalEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("legalEntity", "idexists", "A new legalEntity cannot already have an ID")).body(null);
        }
        LegalEntity result = legalEntityService.save(legalEntity);
        return ResponseEntity.created(new URI("/api/legal-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("legalEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /legal-entities : Updates an existing legalEntity.
     *
     * @param legalEntity the legalEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated legalEntity,
     * or with status 400 (Bad Request) if the legalEntity is not valid,
     * or with status 500 (Internal Server Error) if the legalEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/legal-entities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> updateLegalEntity(@Valid @RequestBody LegalEntity legalEntity) throws URISyntaxException {
        log.debug("REST request to update LegalEntity : {}", legalEntity);
        if (legalEntity.getId() == null) {
            return createLegalEntity(legalEntity);
        }
        LegalEntity result = legalEntityService.save(legalEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("legalEntity", legalEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /legal-entities : get all the legalEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of legalEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/legal-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LegalEntity>> getAllLegalEntities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LegalEntities");
        Page<LegalEntity> page = legalEntityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/legal-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /legal-entities/:id : get the "id" legalEntity.
     *
     * @param id the id of the legalEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the legalEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/legal-entities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> getLegalEntity(@PathVariable Long id) {
        log.debug("REST request to get LegalEntity : {}", id);
        LegalEntity legalEntity = legalEntityService.findOne(id);
        return Optional.ofNullable(legalEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /legal-entities/:id : delete the "id" legalEntity.
     *
     * @param id the id of the legalEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/legal-entities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLegalEntity(@PathVariable Long id) {
        log.debug("REST request to delete LegalEntity : {}", id);
        legalEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("legalEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/legal-entities?query=:query : search for the legalEntity corresponding
     * to the query.
     *
     * @param query the query of the legalEntity search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/legal-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LegalEntity>> searchLegalEntities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LegalEntities for query {}", query);
        Page<LegalEntity> page = legalEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/legal-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
