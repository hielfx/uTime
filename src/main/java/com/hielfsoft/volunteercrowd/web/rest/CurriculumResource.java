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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
     * POST  /curriculums -> Create a new curriculum.
     */
    @RequestMapping(value = "/curriculums",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Curriculum> createCurriculum(@RequestBody Curriculum curriculum) throws URISyntaxException {
        log.debug("REST request to save Curriculum : {}", curriculum);
        if (curriculum.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("curriculum", "idexists", "A new curriculum cannot already have an ID")).body(null);
        }
        Curriculum result = curriculumService.save(curriculum);
        return ResponseEntity.created(new URI("/api/curriculums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("curriculum", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /curriculums -> Updates an existing curriculum.
     */
    @RequestMapping(value = "/curriculums",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Curriculum> updateCurriculum(@RequestBody Curriculum curriculum) throws URISyntaxException {
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
     * GET  /curriculums -> get all the curriculums.
     */
    @RequestMapping(value = "/curriculums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Curriculum>> getAllCurriculums(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("naturalperson-is-null".equals(filter)) {
            log.debug("REST request to get all Curriculums where naturalPerson is null");
            return new ResponseEntity<>(curriculumService.findAllWhereNaturalPersonIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Curriculums");
        Page<Curriculum> page = curriculumService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/curriculums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /curriculums/:id -> get the "id" curriculum.
     */
    @RequestMapping(value = "/curriculums/{id}",
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
     * DELETE  /curriculums/:id -> delete the "id" curriculum.
     */
    @RequestMapping(value = "/curriculums/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
        log.debug("REST request to delete Curriculum : {}", id);
        curriculumService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("curriculum", id.toString())).build();
    }

    /**
     * SEARCH  /_search/curriculums/:query -> search for the curriculum corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/curriculums/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Curriculum> searchCurriculums(@PathVariable String query) {
        log.debug("Request to search Curriculums for query {}", query);
        return curriculumService.search(query);
    }
}
