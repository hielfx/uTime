package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Curriculum;
import com.hielfsoft.volunteercrowd.repository.CurriculumRepository;
import com.hielfsoft.volunteercrowd.repository.search.CurriculumSearchRepository;
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
 * Service Implementation for managing Curriculum.
 */
@Service
@Transactional
public class CurriculumService {

    private final Logger log = LoggerFactory.getLogger(CurriculumService.class);
    
    @Inject
    private CurriculumRepository curriculumRepository;
    
    @Inject
    private CurriculumSearchRepository curriculumSearchRepository;
    
    /**
     * Save a curriculum.
     * @return the persisted entity
     */
    public Curriculum save(Curriculum curriculum) {
        log.debug("Request to save Curriculum : {}", curriculum);
        Curriculum result = curriculumRepository.save(curriculum);
        curriculumSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the curriculums.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Curriculum> findAll(Pageable pageable) {
        log.debug("Request to get all Curriculums");
        Page<Curriculum> result = curriculumRepository.findAll(pageable); 
        return result;
    }


    /**
     *  get all the curriculums where NaturalPerson is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Curriculum> findAllWhereNaturalPersonIsNull() {
        log.debug("Request to get all curriculums where NaturalPerson is null");
        return StreamSupport
            .stream(curriculumRepository.findAll().spliterator(), false)
            .filter(curriculum -> curriculum.getNaturalPerson() == null)
            .collect(Collectors.toList());
    }

    /**
     *  get one curriculum by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Curriculum findOne(Long id) {
        log.debug("Request to get Curriculum : {}", id);
        Curriculum curriculum = curriculumRepository.findOne(id);
        return curriculum;
    }

    /**
     *  delete the  curriculum by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Curriculum : {}", id);
        curriculumRepository.delete(id);
        curriculumSearchRepository.delete(id);
    }

    /**
     * search for the curriculum corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Curriculum> search(String query) {
        
        log.debug("REST request to search Curriculums for query {}", query);
        return StreamSupport
            .stream(curriculumSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
