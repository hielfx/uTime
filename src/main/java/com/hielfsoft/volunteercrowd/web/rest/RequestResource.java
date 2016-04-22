package com.hielfsoft.volunteercrowd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hielfsoft.volunteercrowd.domain.Request;
import com.hielfsoft.volunteercrowd.service.RequestService;
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
 * REST controller for managing Request.
 */
@RestController
@RequestMapping("/api")
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);
        
    @Inject
    private RequestService requestService;
    
    /**
     * POST  /requests : Create a new request.
     *
     * @param request the request to create
     * @return the ResponseEntity with status 201 (Created) and with body the new request, or with status 400 (Bad Request) if the request has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Request> createRequest(@Valid @RequestBody Request request) throws URISyntaxException {
        log.debug("REST request to save Request : {}", request);
        if (request.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("request", "idexists", "A new request cannot already have an ID")).body(null);
        }
        Request result = requestService.save(request);
        return ResponseEntity.created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("request", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requests : Updates an existing request.
     *
     * @param request the request to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated request,
     * or with status 400 (Bad Request) if the request is not valid,
     * or with status 500 (Internal Server Error) if the request couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Request> updateRequest(@Valid @RequestBody Request request) throws URISyntaxException {
        log.debug("REST request to update Request : {}", request);
        if (request.getId() == null) {
            return createRequest(request);
        }
        Request result = requestService.save(request);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("request", request.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requests : get all the requests.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of requests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Request>> getAllRequests(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("payment-is-null".equals(filter)) {
            log.debug("REST request to get all Requests where payment is null");
            return new ResponseEntity<>(requestService.findAllWherePaymentIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Requests");
        Page<Request> page = requestService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requests/:id : get the "id" request.
     *
     * @param id the id of the request to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the request, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Request> getRequest(@PathVariable Long id) {
        log.debug("REST request to get Request : {}", id);
        Request request = requestService.findOne(id);
        return Optional.ofNullable(request)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /requests/:id : delete the "id" request.
     *
     * @param id the id of the request to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        log.debug("REST request to delete Request : {}", id);
        requestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("request", id.toString())).build();
    }

    /**
     * SEARCH  /_search/requests?query=:query : search for the request corresponding
     * to the query.
     *
     * @param query the query of the request search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Request>> searchRequests(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Requests for query {}", query);
        Page<Request> page = requestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
