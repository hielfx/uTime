package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Request;
import com.hielfsoft.volunteercrowd.repository.RequestRepository;
import com.hielfsoft.volunteercrowd.repository.search.RequestSearchRepository;
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
 * Service Implementation for managing Request.
 */
@Service
@Transactional
public class RequestService {

    private final Logger log = LoggerFactory.getLogger(RequestService.class);
    
    @Inject
    private RequestRepository requestRepository;
    
    @Inject
    private RequestSearchRepository requestSearchRepository;
    
    /**
     * Save a request.
     * @return the persisted entity
     */
    public Request save(Request request) {
        log.debug("Request to save Request : {}", request);
        Request result = requestRepository.save(request);
        requestSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the requests.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Request> findAll(Pageable pageable) {
        log.debug("Request to get all Requests");
        Page<Request> result = requestRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one request by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Request findOne(Long id) {
        log.debug("Request to get Request : {}", id);
        Request request = requestRepository.findOne(id);
        return request;
    }

    /**
     *  delete the  request by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Request : {}", id);
        requestRepository.delete(id);
        requestSearchRepository.delete(id);
    }

    /**
     * search for the request corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Request> search(String query) {
        
        log.debug("REST request to search Requests for query {}", query);
        return StreamSupport
            .stream(requestSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
