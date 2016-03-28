package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Ability;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ability entity.
 */
public interface AbilitySearchRepository extends ElasticsearchRepository<Ability, Long> {
}
