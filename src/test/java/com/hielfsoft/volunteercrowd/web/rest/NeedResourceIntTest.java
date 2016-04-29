package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Need;
import com.hielfsoft.volunteercrowd.repository.NeedRepository;
import com.hielfsoft.volunteercrowd.repository.search.NeedSearchRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;
import com.hielfsoft.volunteercrowd.service.NeedService;
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
 * Test class for the NeedResource REST controller.
 *
 * @see NeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class NeedResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CATEGORY = "AAAAA";
    private static final String UPDATED_CATEGORY = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_DATE);

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final long APPUSER_ID = 21;

    @Inject
    private NeedRepository needRepository;

    @Inject
    private NeedService needService;

    @Inject
    private NeedSearchRepository needSearchRepository;

    @Inject
    private AppUserService appUserService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNeedMockMvc;

    private Need need;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NeedResource needResource = new NeedResource();
        ReflectionTestUtils.setField(needResource, "needService", needService);
        this.restNeedMockMvc = MockMvcBuilders.standaloneSetup(needResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        needSearchRepository.deleteAll();
        need = new Need();
        need.setTitle(DEFAULT_TITLE);
        need.setDescription(DEFAULT_DESCRIPTION);
        need.setCategory(DEFAULT_CATEGORY);
        need.setDeleted(DEFAULT_DELETED);
        need.setLocation(DEFAULT_LOCATION);
        need.setCreationDate(DEFAULT_CREATION_DATE);
        need.setModificationDate(DEFAULT_MODIFICATION_DATE);
        need.setCompleted(DEFAULT_COMPLETED);

        need.setAppUser(appUserService.findOne(APPUSER_ID));

    }

    @Test
    @Transactional
    public void createNeed() throws Exception {
        int databaseSizeBeforeCreate = needRepository.findAll().size();

        // Create the Need

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isCreated());

        // Validate the Need in the database
        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeCreate + 1);
        Need testNeed = needs.get(needs.size() - 1);
        assertThat(testNeed.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNeed.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNeed.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testNeed.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testNeed.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testNeed.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testNeed.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testNeed.isCompleted()).isEqualTo(DEFAULT_COMPLETED);

        // Validate the Need in ElasticSearch
        Need needEs = needSearchRepository.findOne(testNeed.getId());
        assertThat(needEs).isEqualToComparingFieldByField(testNeed);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setTitle(null);

        // Create the Need, which fails.

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isBadRequest());

        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setDescription(null);

        // Create the Need, which fails.

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isBadRequest());

        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setCategory(null);

        // Create the Need, which fails.

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isBadRequest());

        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setLocation(null);

        // Create the Need, which fails.

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isBadRequest());

        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setCreationDate(null);

        // Create the Need, which fails.

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isBadRequest());

        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModificationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = needRepository.findAll().size();
        // set the field null
        need.setModificationDate(null);

        // Create the Need, which fails.

        restNeedMockMvc.perform(post("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(need)))
                .andExpect(status().isBadRequest());

        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNeeds() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        // Get all the needs
        restNeedMockMvc.perform(get("/api/needs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(need.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getNeed() throws Exception {
        // Initialize the database
        needRepository.saveAndFlush(need);

        // Get the need
        restNeedMockMvc.perform(get("/api/needs/{id}", need.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(need.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE_STR))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNeed() throws Exception {
        // Get the need
        restNeedMockMvc.perform(get("/api/needs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNeed() throws Exception {
        // Initialize the database
        needService.save(need);

        int databaseSizeBeforeUpdate = needRepository.findAll().size();

        // Update the need
        Need updatedNeed = new Need();
        updatedNeed.setId(need.getId());
        updatedNeed.setTitle(UPDATED_TITLE);
        updatedNeed.setDescription(UPDATED_DESCRIPTION);
        updatedNeed.setCategory(UPDATED_CATEGORY);
        updatedNeed.setDeleted(UPDATED_DELETED);
        updatedNeed.setLocation(UPDATED_LOCATION);
        updatedNeed.setCreationDate(UPDATED_CREATION_DATE);
        updatedNeed.setModificationDate(UPDATED_MODIFICATION_DATE);
        updatedNeed.setCompleted(UPDATED_COMPLETED);

        updatedNeed.setAppUser(appUserService.findOne(APPUSER_ID));

        restNeedMockMvc.perform(put("/api/needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNeed)))
                .andExpect(status().isOk());

        // Validate the Need in the database
        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeUpdate);
        Need testNeed = needs.get(needs.size() - 1);
        assertThat(testNeed.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNeed.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNeed.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testNeed.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testNeed.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testNeed.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testNeed.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testNeed.isCompleted()).isEqualTo(UPDATED_COMPLETED);

        // Validate the Need in ElasticSearch
        Need needEs = needSearchRepository.findOne(testNeed.getId());
        assertThat(needEs).isEqualToComparingFieldByField(testNeed);
    }

    @Test
    @Transactional
    public void deleteNeed() throws Exception {
        // Initialize the database
        needService.save(need);

        int databaseSizeBeforeDelete = needRepository.findAll().size();

        // Get the need
        restNeedMockMvc.perform(delete("/api/needs/{id}", need.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean needExistsInEs = needSearchRepository.exists(need.getId());
        assertThat(needExistsInEs).isFalse();

        // Validate the database is empty
        List<Need> needs = needRepository.findAll();
        assertThat(needs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNeed() throws Exception {
        // Initialize the database
        needService.save(need);

        // Search the need
        restNeedMockMvc.perform(get("/api/_search/needs?query=id:" + need.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(need.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
}
