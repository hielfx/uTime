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
     *
     * @param curriculum the entity to save
     * @return the persisted entity
     */
    public Curriculum save(Curriculum curriculum) {
        log.debug("Request to save Curriculum : {}", curriculum);

        //We add the curriculum to the NaturalPerson
        curriculum.getNaturalPerson().setCurriculum(curriculum);

        Curriculum result = curriculumRepository.save(curriculum);
        curriculumSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the curricula.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Curriculum> findAll(Pageable pageable) {
        log.debug("Request to get all Curricula");
        Page<Curriculum> result = curriculumRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the curricula where NaturalPerson is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Curriculum> findAllWhereNaturalPersonIsNull() {
        log.debug("Request to get all curricula where NaturalPerson is null");
        return StreamSupport
            .stream(curriculumRepository.findAll().spliterator(), false)
            .filter(curriculum -> curriculum.getNaturalPerson() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one curriculum by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Curriculum findOne(Long id) {
        log.debug("Request to get Curriculum : {}", id);
        Curriculum curriculum = curriculumRepository.findOne(id);
        return curriculum;
    }

    /**
     *  Delete the  curriculum by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Curriculum : {}", id);
        curriculumRepository.delete(id);
        curriculumSearchRepository.delete(id);
    }

    /**
     * Search for the curriculum corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Curriculum> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Curricula for query {}", query);
        return curriculumSearchRepository.search(queryStringQuery(query), pageable);
    }
}
