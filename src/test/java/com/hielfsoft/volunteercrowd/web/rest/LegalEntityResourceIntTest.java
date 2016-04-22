package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.repository.LegalEntityRepository;
import com.hielfsoft.volunteercrowd.service.LegalEntityService;
import com.hielfsoft.volunteercrowd.repository.search.LegalEntitySearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LegalEntityResource REST controller.
 *
 * @see LegalEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class LegalEntityResourceIntTest {

    private static final String DEFAULT_MISSION = "AAAAA";
    private static final String UPDATED_MISSION = "BBBBB";
    private static final String DEFAULT_VISION = "AAAAA";
    private static final String UPDATED_VISION = "BBBBB";
    private static final String DEFAULT_WEBSITE = "http://www.google.es";
    private static final String UPDATED_WEBSITE = "http://www.com";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private LegalEntityRepository legalEntityRepository;

    @Inject
    private LegalEntityService legalEntityService;

    @Inject
    private LegalEntitySearchRepository legalEntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLegalEntityMockMvc;

    private LegalEntity legalEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LegalEntityResource legalEntityResource = new LegalEntityResource();
        ReflectionTestUtils.setField(legalEntityResource, "legalEntityService", legalEntityService);
        this.restLegalEntityMockMvc = MockMvcBuilders.standaloneSetup(legalEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        legalEntitySearchRepository.deleteAll();
        legalEntity = new LegalEntity();
        legalEntity.setMission(DEFAULT_MISSION);
        legalEntity.setVision(DEFAULT_VISION);
        legalEntity.setWebsite(DEFAULT_WEBSITE);
        legalEntity.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createLegalEntity() throws Exception {
        int databaseSizeBeforeCreate = legalEntityRepository.findAll().size();

        // Create the LegalEntity

        restLegalEntityMockMvc.perform(post("/api/legal-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalEntity)))
                .andExpect(status().isCreated());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntities = legalEntityRepository.findAll();
        assertThat(legalEntities).hasSize(databaseSizeBeforeCreate + 1);
        LegalEntity testLegalEntity = legalEntities.get(legalEntities.size() - 1);
        assertThat(testLegalEntity.getMission()).isEqualTo(DEFAULT_MISSION);
        assertThat(testLegalEntity.getVision()).isEqualTo(DEFAULT_VISION);
        assertThat(testLegalEntity.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testLegalEntity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the LegalEntity in ElasticSearch
        LegalEntity legalEntityEs = legalEntitySearchRepository.findOne(testLegalEntity.getId());
        assertThat(legalEntityEs).isEqualToComparingFieldByField(testLegalEntity);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalEntityRepository.findAll().size();
        // set the field null
        legalEntity.setDescription(null);

        // Create the LegalEntity, which fails.

        restLegalEntityMockMvc.perform(post("/api/legal-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalEntity)))
                .andExpect(status().isBadRequest());

        List<LegalEntity> legalEntities = legalEntityRepository.findAll();
        assertThat(legalEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLegalEntities() throws Exception {
        // Initialize the database
        legalEntityRepository.saveAndFlush(legalEntity);

        // Get all the legalEntities
        restLegalEntityMockMvc.perform(get("/api/legal-entities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(legalEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].mission").value(hasItem(DEFAULT_MISSION.toString())))
                .andExpect(jsonPath("$.[*].vision").value(hasItem(DEFAULT_VISION.toString())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLegalEntity() throws Exception {
        // Initialize the database
        legalEntityRepository.saveAndFlush(legalEntity);

        // Get the legalEntity
        restLegalEntityMockMvc.perform(get("/api/legal-entities/{id}", legalEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(legalEntity.getId().intValue()))
            .andExpect(jsonPath("$.mission").value(DEFAULT_MISSION.toString()))
            .andExpect(jsonPath("$.vision").value(DEFAULT_VISION.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLegalEntity() throws Exception {
        // Get the legalEntity
        restLegalEntityMockMvc.perform(get("/api/legal-entities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLegalEntity() throws Exception {
        // Initialize the database
        legalEntityService.save(legalEntity);

        int databaseSizeBeforeUpdate = legalEntityRepository.findAll().size();

        // Update the legalEntity
        LegalEntity updatedLegalEntity = new LegalEntity();
        updatedLegalEntity.setId(legalEntity.getId());
        updatedLegalEntity.setMission(UPDATED_MISSION);
        updatedLegalEntity.setVision(UPDATED_VISION);
        updatedLegalEntity.setWebsite(UPDATED_WEBSITE);
        updatedLegalEntity.setDescription(UPDATED_DESCRIPTION);

        restLegalEntityMockMvc.perform(put("/api/legal-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLegalEntity)))
                .andExpect(status().isOk());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntities = legalEntityRepository.findAll();
        assertThat(legalEntities).hasSize(databaseSizeBeforeUpdate);
        LegalEntity testLegalEntity = legalEntities.get(legalEntities.size() - 1);
        assertThat(testLegalEntity.getMission()).isEqualTo(UPDATED_MISSION);
        assertThat(testLegalEntity.getVision()).isEqualTo(UPDATED_VISION);
        assertThat(testLegalEntity.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testLegalEntity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the LegalEntity in ElasticSearch
        LegalEntity legalEntityEs = legalEntitySearchRepository.findOne(testLegalEntity.getId());
        assertThat(legalEntityEs).isEqualToComparingFieldByField(testLegalEntity);
    }

    @Test
    @Transactional
    public void deleteLegalEntity() throws Exception {
        // Initialize the database
        legalEntityService.save(legalEntity);

        int databaseSizeBeforeDelete = legalEntityRepository.findAll().size();

        // Get the legalEntity
        restLegalEntityMockMvc.perform(delete("/api/legal-entities/{id}", legalEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean legalEntityExistsInEs = legalEntitySearchRepository.exists(legalEntity.getId());
        assertThat(legalEntityExistsInEs).isFalse();

        // Validate the database is empty
        List<LegalEntity> legalEntities = legalEntityRepository.findAll();
        assertThat(legalEntities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLegalEntity() throws Exception {
        // Initialize the database
        legalEntityService.save(legalEntity);

        // Search the legalEntity
        restLegalEntityMockMvc.perform(get("/api/_search/legal-entities?query=id:" + legalEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].mission").value(hasItem(DEFAULT_MISSION.toString())))
            .andExpect(jsonPath("$.[*].vision").value(hasItem(DEFAULT_VISION.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
