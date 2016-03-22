package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.NaturalPerson;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonSearchRepository;
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
 * REST controller for managing NaturalPerson.
 */
@RestController
@RequestMapping("/api")
public class NaturalPersonResource {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonResource.class);
        
    @Inject
    private NaturalPersonRepository naturalPersonRepository;
    
    @Inject
    private NaturalPersonSearchRepository naturalPersonSearchRepository;
    
    /**
     * POST  /naturalPersons -> Create a new naturalPerson.
     */
    @RequestMapping(value = "/naturalPersons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> createNaturalPerson(@Valid @RequestBody NaturalPerson naturalPerson) throws URISyntaxException {
        log.debug("REST request to save NaturalPerson : {}", naturalPerson);
        if (naturalPerson.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("naturalPerson", "idexists", "A new naturalPerson cannot already have an ID")).body(null);
        }
        NaturalPerson result = naturalPersonRepository.save(naturalPerson);
        naturalPersonSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/naturalPersons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("naturalPerson", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /naturalPersons -> Updates an existing naturalPerson.
     */
    @RequestMapping(value = "/naturalPersons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> updateNaturalPerson(@Valid @RequestBody NaturalPerson naturalPerson) throws URISyntaxException {
        log.debug("REST request to update NaturalPerson : {}", naturalPerson);
        if (naturalPerson.getId() == null) {
            return createNaturalPerson(naturalPerson);
        }
        NaturalPerson result = naturalPersonRepository.save(naturalPerson);
        naturalPersonSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("naturalPerson", naturalPerson.getId().toString()))
            .body(result);
    }

    /**
     * GET  /naturalPersons -> get all the naturalPersons.
     */
    @RequestMapping(value = "/naturalPersons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NaturalPerson>> getAllNaturalPersons(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NaturalPersons");
        Page<NaturalPerson> page = naturalPersonRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/naturalPersons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /naturalPersons/:id -> get the "id" naturalPerson.
     */
    @RequestMapping(value = "/naturalPersons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NaturalPerson> getNaturalPerson(@PathVariable Long id) {
        log.debug("REST request to get NaturalPerson : {}", id);
        NaturalPerson naturalPerson = naturalPersonRepository.findOne(id);
        return Optional.ofNullable(naturalPerson)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /naturalPersons/:id -> delete the "id" naturalPerson.
     */
    @RequestMapping(value = "/naturalPersons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNaturalPerson(@PathVariable Long id) {
        log.debug("REST request to delete NaturalPerson : {}", id);
        naturalPersonRepository.delete(id);
        naturalPersonSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("naturalPerson", id.toString())).build();
    }

    /**
     * SEARCH  /_search/naturalPersons/:query -> search for the naturalPerson corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/naturalPersons/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NaturalPerson> searchNaturalPersons(@PathVariable String query) {
        log.debug("REST request to search NaturalPersons for query {}", query);
        return StreamSupport
            .stream(naturalPersonSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
