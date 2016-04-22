package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Curriculum;
import com.hielfsoft.volunteercrowd.repository.CurriculumRepository;
import com.hielfsoft.volunteercrowd.service.CurriculumService;
import com.hielfsoft.volunteercrowd.repository.search.CurriculumSearchRepository;

import com.hielfsoft.volunteercrowd.service.NaturalPersonService;
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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CurriculumResource REST controller.
 *
 * @see CurriculumResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class CurriculumResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_WEBSITE = "http://www.google.es";
    private static final String UPDATED_WEBSITE = "http://www.google.com";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_DATE);

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_STATEMENT = "AAAAA";
    private static final String UPDATED_STATEMENT = "BBBBB";
    private static final String DEFAULT_VISION = "AAAAA";
    private static final String UPDATED_VISION = "BBBBB";
    private static final String DEFAULT_MISSION = "AAAAA";
    private static final String UPDATED_MISSION = "BBBBB";

    private static final long NATURAL_PERSON_ID = 23;

    @Inject
    private CurriculumRepository curriculumRepository;

    @Inject
    private CurriculumService curriculumService;

    @Inject
    private CurriculumSearchRepository curriculumSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private NaturalPersonService naturalPersonService;

    private MockMvc restCurriculumMockMvc;

    private Curriculum curriculum;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CurriculumResource curriculumResource = new CurriculumResource();
        ReflectionTestUtils.setField(curriculumResource, "curriculumService", curriculumService);
        this.restCurriculumMockMvc = MockMvcBuilders.standaloneSetup(curriculumResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        curriculumSearchRepository.deleteAll();
        curriculum = new Curriculum();
        curriculum.setWebsite(DEFAULT_WEBSITE);
        curriculum.setCreationDate(DEFAULT_CREATION_DATE);
        curriculum.setModificationDate(DEFAULT_MODIFICATION_DATE);
        curriculum.setFile(DEFAULT_FILE);
        curriculum.setFileContentType(DEFAULT_FILE_CONTENT_TYPE);
        curriculum.setStatement(DEFAULT_STATEMENT);
        curriculum.setVision(DEFAULT_VISION);
        curriculum.setMission(DEFAULT_MISSION);

        curriculum.setNaturalPerson(naturalPersonService.findOne(NATURAL_PERSON_ID));
    }

    @Test
    @Transactional
    public void createCurriculum() throws Exception {
        int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

        // Create the Curriculum

        restCurriculumMockMvc.perform(post("/api/curricula")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(curriculum)))
                .andExpect(status().isCreated());

        // Validate the Curriculum in the database
        List<Curriculum> curricula = curriculumRepository.findAll();
        assertThat(curricula).hasSize(databaseSizeBeforeCreate + 1);
        Curriculum testCurriculum = curricula.get(curricula.size() - 1);
        assertThat(testCurriculum.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCurriculum.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCurriculum.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
        assertThat(testCurriculum.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testCurriculum.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testCurriculum.getStatement()).isEqualTo(DEFAULT_STATEMENT);
        assertThat(testCurriculum.getVision()).isEqualTo(DEFAULT_VISION);
        assertThat(testCurriculum.getMission()).isEqualTo(DEFAULT_MISSION);

        // Validate the Curriculum in ElasticSearch
        Curriculum curriculumEs = curriculumSearchRepository.findOne(testCurriculum.getId());
        assertThat(curriculumEs).isEqualToComparingFieldByField(testCurriculum);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = curriculumRepository.findAll().size();
        // set the field null
        curriculum.setCreationDate(null);

        // Create the Curriculum, which fails.

        restCurriculumMockMvc.perform(post("/api/curricula")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(curriculum)))
                .andExpect(status().isBadRequest());

        List<Curriculum> curricula = curriculumRepository.findAll();
        assertThat(curricula).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModificationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = curriculumRepository.findAll().size();
        // set the field null
        curriculum.setModificationDate(null);

        // Create the Curriculum, which fails.

        restCurriculumMockMvc.perform(post("/api/curricula")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(curriculum)))
                .andExpect(status().isBadRequest());

        List<Curriculum> curricula = curriculumRepository.findAll();
        assertThat(curricula).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatementIsRequired() throws Exception {
        int databaseSizeBeforeTest = curriculumRepository.findAll().size();
        // set the field null
        curriculum.setStatement(null);

        // Create the Curriculum, which fails.

        restCurriculumMockMvc.perform(post("/api/curricula")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(curriculum)))
                .andExpect(status().isBadRequest());

        List<Curriculum> curricula = curriculumRepository.findAll();
        assertThat(curricula).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCurricula() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        // Get all the curricula
        restCurriculumMockMvc.perform(get("/api/curricula?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(curriculum.getId().intValue())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
                .andExpect(jsonPath("$.[*].statement").value(hasItem(DEFAULT_STATEMENT.toString())))
                .andExpect(jsonPath("$.[*].vision").value(hasItem(DEFAULT_VISION.toString())))
                .andExpect(jsonPath("$.[*].mission").value(hasItem(DEFAULT_MISSION.toString())));
    }

    @Test
    @Transactional
    public void getCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        // Get the curriculum
        restCurriculumMockMvc.perform(get("/api/curricula/{id}", curriculum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(curriculum.getId().intValue()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE_STR))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.statement").value(DEFAULT_STATEMENT.toString()))
            .andExpect(jsonPath("$.vision").value(DEFAULT_VISION.toString()))
            .andExpect(jsonPath("$.mission").value(DEFAULT_MISSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCurriculum() throws Exception {
        // Get the curriculum
        restCurriculumMockMvc.perform(get("/api/curricula/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurriculum() throws Exception {
        // Initialize the database
        curriculumService.save(curriculum);

        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

        // Update the curriculum
        Curriculum updatedCurriculum = new Curriculum();
        updatedCurriculum.setId(curriculum.getId());
        updatedCurriculum.setWebsite(UPDATED_WEBSITE);
        updatedCurriculum.setCreationDate(UPDATED_CREATION_DATE);
        updatedCurriculum.setModificationDate(UPDATED_MODIFICATION_DATE);
        updatedCurriculum.setFile(UPDATED_FILE);
        updatedCurriculum.setFileContentType(UPDATED_FILE_CONTENT_TYPE);
        updatedCurriculum.setStatement(UPDATED_STATEMENT);
        updatedCurriculum.setVision(UPDATED_VISION);
        updatedCurriculum.setMission(UPDATED_MISSION);

        restCurriculumMockMvc.perform(put("/api/curricula")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCurriculum)))
                .andExpect(status().isOk());

        // Validate the Curriculum in the database
        List<Curriculum> curricula = curriculumRepository.findAll();
        assertThat(curricula).hasSize(databaseSizeBeforeUpdate);
        Curriculum testCurriculum = curricula.get(curricula.size() - 1);
        assertThat(testCurriculum.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCurriculum.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCurriculum.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
        assertThat(testCurriculum.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testCurriculum.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testCurriculum.getStatement()).isEqualTo(UPDATED_STATEMENT);
        assertThat(testCurriculum.getVision()).isEqualTo(UPDATED_VISION);
        assertThat(testCurriculum.getMission()).isEqualTo(UPDATED_MISSION);

        // Validate the Curriculum in ElasticSearch
        Curriculum curriculumEs = curriculumSearchRepository.findOne(testCurriculum.getId());
        assertThat(curriculumEs).isEqualToComparingFieldByField(testCurriculum);
    }

    @Test
    @Transactional
    public void deleteCurriculum() throws Exception {
        // Initialize the database
        curriculumService.save(curriculum);

        int databaseSizeBeforeDelete = curriculumRepository.findAll().size();

        // Get the curriculum
        restCurriculumMockMvc.perform(delete("/api/curricula/{id}", curriculum.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean curriculumExistsInEs = curriculumSearchRepository.exists(curriculum.getId());
        assertThat(curriculumExistsInEs).isFalse();

        // Validate the database is empty
        List<Curriculum> curricula = curriculumRepository.findAll();
        assertThat(curricula).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCurriculum() throws Exception {
        // Initialize the database
        curriculumService.save(curriculum);

        // Search the curriculum
        restCurriculumMockMvc.perform(get("/api/_search/curricula?query=id:" + curriculum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curriculum.getId().intValue())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].statement").value(hasItem(DEFAULT_STATEMENT.toString())))
            .andExpect(jsonPath("$.[*].vision").value(hasItem(DEFAULT_VISION.toString())))
            .andExpect(jsonPath("$.[*].mission").value(hasItem(DEFAULT_MISSION.toString())));
    }
}
