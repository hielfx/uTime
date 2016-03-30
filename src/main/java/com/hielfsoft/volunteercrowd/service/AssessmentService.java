package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Assessment;
import com.hielfsoft.volunteercrowd.repository.AssessmentRepository;
import com.hielfsoft.volunteercrowd.repository.search.AssessmentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
     * @return the persisted entity
     */
    public Assessment save(Assessment assessment) {
        log.debug("Request to save Assessment : {}", assessment);
        Assessment result = assessmentRepository.save(assessment);
        assessmentSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the assessments.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Assessment> findAll(Pageable pageable) {
        log.debug("Request to get all Assessments");
        Page<Assessment> result = assessmentRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one assessment by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Assessment findOne(Long id) {
        log.debug("Request to get Assessment : {}", id);
        Assessment assessment = assessmentRepository.findOne(id);
        return assessment;
    }

    /**
     *  delete the  assessment by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Assessment : {}", id);
        assessmentRepository.delete(id);
        assessmentSearchRepository.delete(id);
    }

    /**
     * search for the assessment corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Assessment> search(String query) {
        
        log.debug("REST request to search Assessments for query {}", query);
        return StreamSupport
            .stream(assessmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
