package com.hielfsoft.volunteercrowd.repository.search;

import com.hielfsoft.volunteercrowd.domain.AppUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AppUser entity.
 */
public interface AppUserSearchRepository extends ElasticsearchRepository<AppUser, Long> {
}
