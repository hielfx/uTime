package com.hielfsoft.volunteercrowd.service;

import com.hielfsoft.volunteercrowd.domain.Tag;
import com.hielfsoft.volunteercrowd.repository.TagRepository;
import com.hielfsoft.volunteercrowd.repository.search.TagSearchRepository;
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
 * Service Implementation for managing Tag.
 */
@Service
@Transactional
public class TagService {

    private final Logger log = LoggerFactory.getLogger(TagService.class);
    
    @Inject
    private TagRepository tagRepository;
    
    @Inject
    private TagSearchRepository tagSearchRepository;
    
    /**
     * Save a tag.
     * @return the persisted entity
     */
    public Tag save(Tag tag) {
        log.debug("Request to save Tag : {}", tag);
        Tag result = tagRepository.save(tag);
        tagSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the tags.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Tag> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        Page<Tag> result = tagRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one tag by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Tag findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        Tag tag = tagRepository.findOne(id);
        return tag;
    }

    /**
     *  delete the  tag by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.delete(id);
        tagSearchRepository.delete(id);
    }

    /**
     * search for the tag corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Tag> search(String query) {
        
        log.debug("REST request to search Tags for query {}", query);
        return StreamSupport
            .stream(tagSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
