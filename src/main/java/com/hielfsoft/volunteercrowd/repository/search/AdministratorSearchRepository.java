package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.Administrator;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Administrator entity.
 */
public interface AdministratorSearchRepository extends ElasticsearchRepository<Administrator, Long> {
}
