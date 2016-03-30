package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Assessment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Assessment entity.
 */
public interface AssessmentSearchRepository extends ElasticsearchRepository<Assessment, Long> {
}
