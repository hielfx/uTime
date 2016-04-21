package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Curriculum;
import com.hielfsoft.volunteercrowd.service.CurriculumService;
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
 * REST controller for managing Curriculum.
 */
@RestController
@RequestMapping("/api")
public class CurriculumResource {

    private final Logger log = LoggerFactory.getLogger(CurriculumResource.class);

    @Inject
    private CurriculumService curriculumService;

    /**
     * POST  /curricula : Create a new curriculum.
     *
     * @param curriculum the curriculum to create
     * @return the ResponseEntity with status 201 (Created) and with body the new curriculum, or with status 400 (Bad Request) if the curriculum has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/curricula",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Curriculum> createCurriculum(@Valid @RequestBody Curriculum curriculum) throws URISyntaxException {
        log.debug("REST request to save Curriculum : {}", curriculum);
        if (curriculum.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("curriculum", "idexists", "A new curriculum cannot already have an ID")).body(null);
        }
        Curriculum result = curriculumService.save(curriculum);
        return ResponseEntity.created(new URI("/api/curricula/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("curriculum", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /curricula : Updates an existing curriculum.
     *
     * @param curriculum the curriculum to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated curriculum,
     * or with status 400 (Bad Request) if the curriculum is not valid,
     * or with status 500 (Internal Server Error) if the curriculum couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/curricula",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Curriculum> updateCurriculum(@Valid @RequestBody Curriculum curriculum) throws URISyntaxException {
        log.debug("REST request to update Curriculum : {}", curriculum);
        if (curriculum.getId() == null) {
            return createCurriculum(curriculum);
        }
        Curriculum result = curriculumService.save(curriculum);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("curriculum", curriculum.getId().toString()))
            .body(result);
    }

    /**
     * GET  /curricula : get all the curricula.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of curricula in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/curricula",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Curriculum>> getAllCurricula(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("naturalperson-is-null".equals(filter)) {
            log.debug("REST request to get all Curriculums where naturalPerson is null");
            return new ResponseEntity<>(curriculumService.findAllWhereNaturalPersonIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Curricula");
        Page<Curriculum> page = curriculumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/curricula");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /curricula/:id : get the "id" curriculum.
     *
     * @param id the id of the curriculum to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the curriculum, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/curricula/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Curriculum> getCurriculum(@PathVariable Long id) {
        log.debug("REST request to get Curriculum : {}", id);
        Curriculum curriculum = curriculumService.findOne(id);
        return Optional.ofNullable(curriculum)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /curricula/:id : delete the "id" curriculum.
     *
     * @param id the id of the curriculum to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/curricula/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        log.debug("REST request to delete Curriculum : {}", id);
        curriculumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("curriculum", id.toString())).build();
    }

    /**
     * SEARCH  /_search/curricula?query=:query : search for the curriculum corresponding
     * to the query.
     *
     * @param query the query of the curriculum search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/curricula",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Curriculum>> searchCurricula(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Curricula for query {}", query);
        Page<Curriculum> page = curriculumService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/curricula");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
