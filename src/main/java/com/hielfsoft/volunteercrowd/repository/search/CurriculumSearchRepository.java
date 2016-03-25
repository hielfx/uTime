package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Curriculum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Curriculum entity.
 */
public interface CurriculumSearchRepository extends ElasticsearchRepository<Curriculum, Long> {
}
