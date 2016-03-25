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
     * POST  /legalEntitys -> Create a new legalEntity.
     */
    @RequestMapping(value = "/legalEntitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> createLegalEntity(@RequestBody LegalEntity legalEntity) throws URISyntaxException {
        log.debug("REST request to save LegalEntity : {}", legalEntity);
        if (legalEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("legalEntity", "idexists", "A new legalEntity cannot already have an ID")).body(null);
        }
        LegalEntity result = legalEntityService.save(legalEntity);
        return ResponseEntity.created(new URI("/api/legalEntitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("legalEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /legalEntitys -> Updates an existing legalEntity.
     */
    @RequestMapping(value = "/legalEntitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LegalEntity> updateLegalEntity(@RequestBody LegalEntity legalEntity) throws URISyntaxException {
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
     * GET  /legalEntitys -> get all the legalEntitys.
     */
    @RequestMapping(value = "/legalEntitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LegalEntity>> getAllLegalEntitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LegalEntitys");
        Page<LegalEntity> page = legalEntityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/legalEntitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /legalEntitys/:id -> get the "id" legalEntity.
     */
    @RequestMapping(value = "/legalEntitys/{id}",
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
     * DELETE  /legalEntitys/:id -> delete the "id" legalEntity.
     */
    @RequestMapping(value = "/legalEntitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLegalEntity(@PathVariable Long id) {
        log.debug("REST request to delete LegalEntity : {}", id);
        legalEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("legalEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/legalEntitys/:query -> search for the legalEntity corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/legalEntitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LegalEntity> searchLegalEntitys(@PathVariable String query) {
        log.debug("Request to search LegalEntitys for query {}", query);
        return legalEntityService.search(query);
    }
}
