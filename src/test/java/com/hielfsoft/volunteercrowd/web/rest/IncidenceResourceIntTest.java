package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Incidence;
import com.hielfsoft.volunteercrowd.repository.IncidenceRepository;
import com.hielfsoft.volunteercrowd.repository.search.IncidenceSearchRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;
import com.hielfsoft.volunteercrowd.service.IncidenceService;
import com.hielfsoft.volunteercrowd.service.RequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the IncidenceResource REST controller.
 *
 * @see IncidenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class IncidenceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final Boolean DEFAULT_CLOSED = false;
    private static final Boolean UPDATED_CLOSED = true;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ADMIN_COMMENT = "AAAAA";
    private static final String UPDATED_ADMIN_COMMENT = "BBBBB";

    private static final long CREATOR_ID = 12;
    private static final long REQUEST_ID = 47;

    @Inject
    private IncidenceRepository incidenceRepository;

    @Inject
    private IncidenceService incidenceService;

    @Inject
    private IncidenceSearchRepository incidenceSearchRepository;

    @Inject
    private AppUserService appUserService;

    @Inject
    private RequestService requestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restIncidenceMockMvc;

    private Incidence incidence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IncidenceResource incidenceResource = new IncidenceResource();
        ReflectionTestUtils.setField(incidenceResource, "incidenceService", incidenceService);
        this.restIncidenceMockMvc = MockMvcBuilders.standaloneSetup(incidenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        incidenceSearchRepository.deleteAll();
        incidence = new Incidence();
        incidence.setCreationDate(DEFAULT_CREATION_DATE);
        incidence.setClosed(DEFAULT_CLOSED);
        incidence.setDescription(DEFAULT_DESCRIPTION);
        incidence.setAdminComment(DEFAULT_ADMIN_COMMENT);

        incidence.setRequest(requestService.findOne(REQUEST_ID));
        incidence.setCreator(appUserService.findOne(CREATOR_ID));
    }

    @Test
    @Transactional
    public void createIncidence() throws Exception {
        int databaseSizeBeforeCreate = incidenceRepository.findAll().size();

        // Create the Incidence

        restIncidenceMockMvc.perform(post("/api/incidences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incidence)))
                .andExpect(status().isCreated());

        // Validate the Incidence in the database
        List<Incidence> incidences = incidenceRepository.findAll();
        assertThat(incidences).hasSize(databaseSizeBeforeCreate + 1);
        Incidence testIncidence = incidences.get(incidences.size() - 1);
        assertThat(testIncidence.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testIncidence.isClosed()).isEqualTo(DEFAULT_CLOSED);
        assertThat(testIncidence.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIncidence.getAdminComment()).isEqualTo(DEFAULT_ADMIN_COMMENT);

        // Validate the Incidence in ElasticSearch
        Incidence incidenceEs = incidenceSearchRepository.findOne(testIncidence.getId());
        assertThat(incidenceEs).isEqualToComparingFieldByField(testIncidence);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = incidenceRepository.findAll().size();
        // set the field null
        incidence.setCreationDate(null);

        // Create the Incidence, which fails.

        restIncidenceMockMvc.perform(post("/api/incidences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incidence)))
                .andExpect(status().isBadRequest());

        List<Incidence> incidences = incidenceRepository.findAll();
        assertThat(incidences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClosedIsRequired() throws Exception {
        int databaseSizeBeforeTest = incidenceRepository.findAll().size();
        // set the field null
        incidence.setClosed(null);

        // Create the Incidence, which fails.

        restIncidenceMockMvc.perform(post("/api/incidences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incidence)))
                .andExpect(status().isBadRequest());

        List<Incidence> incidences = incidenceRepository.findAll();
        assertThat(incidences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = incidenceRepository.findAll().size();
        // set the field null
        incidence.setDescription(null);

        // Create the Incidence, which fails.

        restIncidenceMockMvc.perform(post("/api/incidences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incidence)))
                .andExpect(status().isBadRequest());

        List<Incidence> incidences = incidenceRepository.findAll();
        assertThat(incidences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIncidences() throws Exception {
        // Initialize the database
        incidenceRepository.saveAndFlush(incidence);

        // Get all the incidences
        restIncidenceMockMvc.perform(get("/api/incidences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(incidence.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.booleanValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].adminComment").value(hasItem(DEFAULT_ADMIN_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getIncidence() throws Exception {
        // Initialize the database
        incidenceRepository.saveAndFlush(incidence);

        // Get the incidence
        restIncidenceMockMvc.perform(get("/api/incidences/{id}", incidence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(incidence.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.closed").value(DEFAULT_CLOSED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.adminComment").value(DEFAULT_ADMIN_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIncidence() throws Exception {
        // Get the incidence
        restIncidenceMockMvc.perform(get("/api/incidences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncidence() throws Exception {
        // Initialize the database
        incidenceService.save(incidence);

        int databaseSizeBeforeUpdate = incidenceRepository.findAll().size();

        // Update the incidence
        Incidence updatedIncidence = new Incidence();
        updatedIncidence.setId(incidence.getId());
        updatedIncidence.setCreationDate(UPDATED_CREATION_DATE);
        updatedIncidence.setClosed(UPDATED_CLOSED);
        updatedIncidence.setDescription(UPDATED_DESCRIPTION);
        updatedIncidence.setAdminComment(UPDATED_ADMIN_COMMENT);

        updatedIncidence.setRequest(requestService.findOne(REQUEST_ID));
        updatedIncidence.setCreator(appUserService.findOne(CREATOR_ID));

        restIncidenceMockMvc.perform(put("/api/incidences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedIncidence)))
                .andExpect(status().isOk());

        // Validate the Incidence in the database
        List<Incidence> incidences = incidenceRepository.findAll();
        assertThat(incidences).hasSize(databaseSizeBeforeUpdate);
        Incidence testIncidence = incidences.get(incidences.size() - 1);
        assertThat(testIncidence.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testIncidence.isClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testIncidence.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIncidence.getAdminComment()).isEqualTo(UPDATED_ADMIN_COMMENT);

        // Validate the Incidence in ElasticSearch
        Incidence incidenceEs = incidenceSearchRepository.findOne(testIncidence.getId());
        assertThat(incidenceEs).isEqualToComparingFieldByField(testIncidence);
    }

    @Test
    @Transactional
    public void deleteIncidence() throws Exception {
        // Initialize the database
        incidenceService.save(incidence);

        int databaseSizeBeforeDelete = incidenceRepository.findAll().size();

        // Get the incidence
        restIncidenceMockMvc.perform(delete("/api/incidences/{id}", incidence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean incidenceExistsInEs = incidenceSearchRepository.exists(incidence.getId());
        assertThat(incidenceExistsInEs).isFalse();

        // Validate the database is empty
        List<Incidence> incidences = incidenceRepository.findAll();
        assertThat(incidences).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIncidence() throws Exception {
        // Initialize the database
        incidenceService.save(incidence);

        // Search the incidence
        restIncidenceMockMvc.perform(get("/api/_search/incidences?query=id:" + incidence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incidence.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].adminComment").value(hasItem(DEFAULT_ADMIN_COMMENT.toString())));
    }
}
