package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.Curriculum;
import com.hielfsoft.volunteercrowd.repository.CurriculumRepository;
import com.hielfsoft.volunteercrowd.service.CurriculumService;

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
import java.time.*;
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
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CurriculumResourceIntTest {

    private static final String DEFAULT_WEBSITE = "http://www.google.es";
    private static final String UPDATED_WEBSITE = "http://www.google.com";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusYears(1);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusMonths(1);

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusYears(1).plusMonths(1);
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusDays(1);

    @Inject
    private CurriculumRepository curriculumRepository;

    @Inject
    private CurriculumService curriculumService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

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
        curriculum = new Curriculum();
        curriculum.setWebsite(DEFAULT_WEBSITE);
        curriculum.setCreationDate(DEFAULT_CREATION_DATE);
        curriculum.setModificationDate(DEFAULT_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void createCurriculum() throws Exception {
        int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

        // Create the Curriculum

        restCurriculumMockMvc.perform(post("/api/curriculums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(curriculum)))
                .andExpect(status().isCreated());

        // Validate the Curriculum in the database
        List<Curriculum> curriculums = curriculumRepository.findAll();
        assertThat(curriculums).hasSize(databaseSizeBeforeCreate + 1);
        Curriculum testCurriculum = curriculums.get(curriculums.size() - 1);
        assertThat(testCurriculum.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCurriculum.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCurriculum.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllCurriculums() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        // Get all the curriculums
        restCurriculumMockMvc.perform(get("/api/curriculums?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(curriculum.getId().intValue())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
                .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void getCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        // Get the curriculum
        restCurriculumMockMvc.perform(get("/api/curriculums/{id}", curriculum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(curriculum.getId().intValue()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCurriculum() throws Exception {
        // Get the curriculum
        restCurriculumMockMvc.perform(get("/api/curriculums/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

		int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

        // Update the curriculum
        curriculum.setWebsite(UPDATED_WEBSITE);
        curriculum.setCreationDate(UPDATED_CREATION_DATE);
        curriculum.setModificationDate(UPDATED_MODIFICATION_DATE);

        restCurriculumMockMvc.perform(put("/api/curriculums")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(curriculum)))
                .andExpect(status().isOk());

        // Validate the Curriculum in the database
        List<Curriculum> curriculums = curriculumRepository.findAll();
        assertThat(curriculums).hasSize(databaseSizeBeforeUpdate);
        Curriculum testCurriculum = curriculums.get(curriculums.size() - 1);
        assertThat(testCurriculum.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCurriculum.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCurriculum.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void deleteCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

		int databaseSizeBeforeDelete = curriculumRepository.findAll().size();

        // Get the curriculum
        restCurriculumMockMvc.perform(delete("/api/curriculums/{id}", curriculum.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Curriculum> curriculums = curriculumRepository.findAll();
        assertThat(curriculums).hasSize(databaseSizeBeforeDelete - 1);
    }
}
