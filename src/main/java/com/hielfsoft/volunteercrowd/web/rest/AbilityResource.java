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
     * POST  /abilities : Create a new ability.
     *
     * @param ability the ability to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ability, or with status 400 (Bad Request) if the ability has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/abilities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ability> createAbility(@Valid @RequestBody Ability ability) throws URISyntaxException {
        log.debug("REST request to save Ability : {}", ability);
        if (ability.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ability", "idexists", "A new ability cannot already have an ID")).body(null);
        }
        Ability result = abilityService.save(ability);
        return ResponseEntity.created(new URI("/api/abilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ability", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /abilities : Updates an existing ability.
     *
     * @param ability the ability to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ability,
     * or with status 400 (Bad Request) if the ability is not valid,
     * or with status 500 (Internal Server Error) if the ability couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/abilities",
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
     * GET  /abilities : get all the abilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of abilities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/abilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ability>> getAllAbilities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Abilities");
        Page<Ability> page = abilityService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/abilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /abilities/:id : get the "id" ability.
     *
     * @param id the id of the ability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ability, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/abilities/{id}",
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
     * DELETE  /abilities/:id : delete the "id" ability.
     *
     * @param id the id of the ability to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/abilities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAbility(@PathVariable Long id) {
        log.debug("REST request to delete Ability : {}", id);
        abilityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ability", id.toString())).build();
    }

    /**
     * SEARCH  /_search/abilities?query=:query : search for the ability corresponding
     * to the query.
     *
     * @param query the query of the ability search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/abilities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ability>> searchAbilities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Abilities for query {}", query);
        Page<Ability> page = abilityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/abilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
