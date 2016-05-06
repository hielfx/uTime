package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.NaturalPersonForm;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonFormRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonFormSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing NaturalPersonForm.
 */
@Service
@Transactional
public class NaturalPersonFormService {

    private final Logger log = LoggerFactory.getLogger(NaturalPersonFormService.class);

    @Inject
    private NaturalPersonFormRepository naturalPersonFormRepository;

    @Inject
    private NaturalPersonFormSearchRepository naturalPersonFormSearchRepository;

    /**
     * Save a naturalPersonForm.
     *
     * @param naturalPersonForm the entity to save
     * @return the persisted entity
     */
    public NaturalPersonForm save(NaturalPersonForm naturalPersonForm) {
        log.debug("Request to save NaturalPersonForm : {}", naturalPersonForm);
        NaturalPersonForm result = naturalPersonFormRepository.save(naturalPersonForm);
        naturalPersonFormSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the naturalPersonForms.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NaturalPersonForm> findAll() {
        log.debug("Request to get all NaturalPersonForms");
        List<NaturalPersonForm> result = naturalPersonFormRepository.findAll();
        return result;
    }

    /**
     * Get one naturalPersonForm by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public NaturalPersonForm findOne(Long id) {
        log.debug("Request to get NaturalPersonForm : {}", id);
        NaturalPersonForm naturalPersonForm = naturalPersonFormRepository.findOne(id);
        return naturalPersonForm;
    }

    /**
     * Delete the  naturalPersonForm by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NaturalPersonForm : {}", id);
        naturalPersonFormRepository.delete(id);
        naturalPersonFormSearchRepository.delete(id);
    }

    /**
     * Search for the naturalPersonForm corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NaturalPersonForm> search(String query) {
        log.debug("Request to search NaturalPersonForms for query {}", query);
        return StreamSupport
            .stream(naturalPersonFormSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
