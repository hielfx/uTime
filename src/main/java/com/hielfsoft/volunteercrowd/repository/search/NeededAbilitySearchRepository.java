package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.NeededAbility;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NeededAbility entity.
 */
public interface NeededAbilitySearchRepository extends ElasticsearchRepository<NeededAbility, Long> {
}
