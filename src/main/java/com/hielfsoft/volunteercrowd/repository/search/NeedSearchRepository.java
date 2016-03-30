package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Need;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Need entity.
 */
public interface NeedSearchRepository extends ElasticsearchRepository<Need, Long> {
}
