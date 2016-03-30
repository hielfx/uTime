package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Assessment;
import com.hielfsoft.volunteercrowd.service.AssessmentService;
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
 * REST controller for managing Assessment.
 */
@RestController
@RequestMapping("/api")
public class AssessmentResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentResource.class);
        
    @Inject
    private AssessmentService assessmentService;
    
    /**
     * POST  /assessments -> Create a new assessment.
     */
    @RequestMapping(value = "/assessments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> createAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to save Assessment : {}", assessment);
        if (assessment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assessment", "idexists", "A new assessment cannot already have an ID")).body(null);
        }
        Assessment result = assessmentService.save(assessment);
        return ResponseEntity.created(new URI("/api/assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assessment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assessments -> Updates an existing assessment.
     */
    @RequestMapping(value = "/assessments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> updateAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}", assessment);
        if (assessment.getId() == null) {
            return createAssessment(assessment);
        }
        Assessment result = assessmentService.save(assessment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("assessment", assessment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assessments -> get all the assessments.
     */
    @RequestMapping(value = "/assessments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Assessment>> getAllAssessments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Assessments");
        Page<Assessment> page = assessmentService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assessments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assessments/:id -> get the "id" assessment.
     */
    @RequestMapping(value = "/assessments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> getAssessment(@PathVariable Long id) {
        log.debug("REST request to get Assessment : {}", id);
        Assessment assessment = assessmentService.findOne(id);
        return Optional.ofNullable(assessment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /assessments/:id -> delete the "id" assessment.
     */
    @RequestMapping(value = "/assessments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.debug("REST request to delete Assessment : {}", id);
        assessmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assessment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/assessments/:query -> search for the assessment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/assessments/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Assessment> searchAssessments(@PathVariable String query) {
        log.debug("Request to search Assessments for query {}", query);
        return assessmentService.search(query);
    }
}
