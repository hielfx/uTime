package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Incidence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Incidence entity.
 */
public interface IncidenceSearchRepository extends ElasticsearchRepository<Incidence, Long> {
}
