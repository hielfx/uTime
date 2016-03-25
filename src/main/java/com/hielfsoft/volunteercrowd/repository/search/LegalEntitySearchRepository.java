package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LegalEntity entity.
 */
public interface LegalEntitySearchRepository extends ElasticsearchRepository<LegalEntity, Long> {
}
