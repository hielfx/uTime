package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Assessment;
import com.hielfsoft.volunteercrowd.repository.AssessmentRepository;
import com.hielfsoft.volunteercrowd.repository.search.AssessmentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Assessment.
 */
@Service
@Transactional
public class AssessmentService {

    private final Logger log = LoggerFactory.getLogger(AssessmentService.class);

    @Inject
    private AssessmentRepository assessmentRepository;

    @Inject
    private AssessmentSearchRepository assessmentSearchRepository;

    /**
     * Save a assessment.
     *
     * @param assessment the entity to save
     * @return the persisted entity
     */
    public Assessment save(Assessment assessment) {
        log.debug("Request to save Assessment : {}", assessment);
        Assessment result = assessmentRepository.save(assessment);
        assessmentSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the assessments.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Assessment> findAll(Pageable pageable) {
        log.debug("Request to get all Assessments");
        Page<Assessment> result = assessmentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one assessment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Assessment findOne(Long id) {
        log.debug("Request to get Assessment : {}", id);
        Assessment assessment = assessmentRepository.findOne(id);
        return assessment;
    }

    /**
     *  Delete the  assessment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Assessment : {}", id);
        assessmentRepository.delete(id);
        assessmentSearchRepository.delete(id);
    }

    /**
     * Search for the assessment corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Assessment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Assessments for query {}", query);
        return assessmentSearchRepository.search(queryStringQuery(query), pageable);
    }
}
