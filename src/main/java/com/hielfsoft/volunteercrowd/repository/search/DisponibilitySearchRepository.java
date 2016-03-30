package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Disponibility;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Disponibility entity.
 */
public interface DisponibilitySearchRepository extends ElasticsearchRepository<Disponibility, Long> {
}
