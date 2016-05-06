package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.NaturalPersonForm;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NaturalPersonForm entity.
 */
public interface NaturalPersonFormSearchRepository extends ElasticsearchRepository<NaturalPersonForm, Long> {
}
