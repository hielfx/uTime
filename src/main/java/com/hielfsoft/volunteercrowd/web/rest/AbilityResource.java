package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Ability;
import com.hielfsoft.volunteercrowd.service.AbilityService;
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
 * REST controller for managing Ability.
 */
@RestController
@RequestMapping("/api")
public class AbilityResource {

    private final Logger log = LoggerFactory.getLogger(AbilityResource.class);
        
    @Inject
    private AbilityService abilityService;
    
    /**
     * POST  /abilitys -> Create a new ability.
     */
    @RequestMapping(value = "/abilitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ability> createAbility(@Valid @RequestBody Ability ability) throws URISyntaxException {
        log.debug("REST request to save Ability : {}", ability);
        if (ability.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ability", "idexists", "A new ability cannot already have an ID")).body(null);
        }
        Ability result = abilityService.save(ability);
        return ResponseEntity.created(new URI("/api/abilitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ability", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /abilitys -> Updates an existing ability.
     */
    @RequestMapping(value = "/abilitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ability> updateAbility(@Valid @RequestBody Ability ability) throws URISyntaxException {
        log.debug("REST request to update Ability : {}", ability);
        if (ability.getId() == null) {
            return createAbility(ability);
        }
        Ability result = abilityService.save(ability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ability", ability.getId().toString()))
            .body(result);
    }

    /**
     * GET  /abilitys -> get all the abilitys.
     */
    @RequestMapping(value = "/abilitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ability>> getAllAbilitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Abilitys");
        Page<Ability> page = abilityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/abilitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /abilitys/:id -> get the "id" ability.
     */
    @RequestMapping(value = "/abilitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ability> getAbility(@PathVariable Long id) {
        log.debug("REST request to get Ability : {}", id);
        Ability ability = abilityService.findOne(id);
        return Optional.ofNullable(ability)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /abilitys/:id -> delete the "id" ability.
     */
    @RequestMapping(value = "/abilitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAbility(@PathVariable Long id) {
        log.debug("REST request to delete Ability : {}", id);
        abilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ability", id.toString())).build();
    }

    /**
     * SEARCH  /_search/abilitys/:query -> search for the ability corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/abilitys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ability> searchAbilitys(@PathVariable String query) {
        log.debug("Request to search Abilitys for query {}", query);
        return abilityService.search(query);
    }
}
