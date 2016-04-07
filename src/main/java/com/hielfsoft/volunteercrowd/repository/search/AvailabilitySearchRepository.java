package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Availability;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Availability entity.
 */
public interface AvailabilitySearchRepository extends ElasticsearchRepository<Availability, Long> {
}
