package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Request;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Request entity.
 */
public interface RequestSearchRepository extends ElasticsearchRepository<Request, Long> {
}
