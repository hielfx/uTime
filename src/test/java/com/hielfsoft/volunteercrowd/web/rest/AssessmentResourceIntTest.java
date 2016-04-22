package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Assessment;
import com.hielfsoft.volunteercrowd.repository.AssessmentRepository;
import com.hielfsoft.volunteercrowd.service.AssessmentService;
import com.hielfsoft.volunteercrowd.repository.search.AssessmentSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AssessmentResource REST controller.
 *
 * @see AssessmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class AssessmentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_MOMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_MOMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_MOMENT_STR = dateTimeFormatter.format(DEFAULT_CREATION_MOMENT);

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    @Inject
    private AssessmentRepository assessmentRepository;

    @Inject
    private AssessmentService assessmentService;

    @Inject
    private AssessmentSearchRepository assessmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAssessmentMockMvc;

    private Assessment assessment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssessmentResource assessmentResource = new AssessmentResource();
        ReflectionTestUtils.setField(assessmentResource, "assessmentService", assessmentService);
        this.restAssessmentMockMvc = MockMvcBuilders.standaloneSetup(assessmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        assessmentSearchRepository.deleteAll();
        assessment = new Assessment();
        assessment.setCreationMoment(DEFAULT_CREATION_MOMENT);
        assessment.setRating(DEFAULT_RATING);
        assessment.setComment(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createAssessment() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();

        // Create the Assessment

        restAssessmentMockMvc.perform(post("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isCreated());

        // Validate the Assessment in the database
        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeCreate + 1);
        Assessment testAssessment = assessments.get(assessments.size() - 1);
        assertThat(testAssessment.getCreationMoment()).isEqualTo(DEFAULT_CREATION_MOMENT);
        assertThat(testAssessment.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testAssessment.getComment()).isEqualTo(DEFAULT_COMMENT);

        // Validate the Assessment in ElasticSearch
        Assessment assessmentEs = assessmentSearchRepository.findOne(testAssessment.getId());
        assertThat(assessmentEs).isEqualToComparingFieldByField(testAssessment);
    }

    @Test
    @Transactional
    public void checkCreationMomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().size();
        // set the field null
        assessment.setCreationMoment(null);

        // Create the Assessment, which fails.

        restAssessmentMockMvc.perform(post("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isBadRequest());

        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().size();
        // set the field null
        assessment.setRating(null);

        // Create the Assessment, which fails.

        restAssessmentMockMvc.perform(post("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isBadRequest());

        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentRepository.findAll().size();
        // set the field null
        assessment.setComment(null);

        // Create the Assessment, which fails.

        restAssessmentMockMvc.perform(post("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isBadRequest());

        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssessments() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get all the assessments
        restAssessmentMockMvc.perform(get("/api/assessments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(assessment.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationMoment").value(hasItem(DEFAULT_CREATION_MOMENT_STR)))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get the assessment
        restAssessmentMockMvc.perform(get("/api/assessments/{id}", assessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(assessment.getId().intValue()))
            .andExpect(jsonPath("$.creationMoment").value(DEFAULT_CREATION_MOMENT_STR))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssessment() throws Exception {
        // Get the assessment
        restAssessmentMockMvc.perform(get("/api/assessments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssessment() throws Exception {
        // Initialize the database
        assessmentService.save(assessment);

        int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment
        Assessment updatedAssessment = new Assessment();
        updatedAssessment.setId(assessment.getId());
        updatedAssessment.setCreationMoment(UPDATED_CREATION_MOMENT);
        updatedAssessment.setRating(UPDATED_RATING);
        updatedAssessment.setComment(UPDATED_COMMENT);

        restAssessmentMockMvc.perform(put("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAssessment)))
                .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessments.get(assessments.size() - 1);
        assertThat(testAssessment.getCreationMoment()).isEqualTo(UPDATED_CREATION_MOMENT);
        assertThat(testAssessment.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testAssessment.getComment()).isEqualTo(UPDATED_COMMENT);

        // Validate the Assessment in ElasticSearch
        Assessment assessmentEs = assessmentSearchRepository.findOne(testAssessment.getId());
        assertThat(assessmentEs).isEqualToComparingFieldByField(testAssessment);
    }

    @Test
    @Transactional
    public void deleteAssessment() throws Exception {
        // Initialize the database
        assessmentService.save(assessment);

        int databaseSizeBeforeDelete = assessmentRepository.findAll().size();

        // Get the assessment
        restAssessmentMockMvc.perform(delete("/api/assessments/{id}", assessment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean assessmentExistsInEs = assessmentSearchRepository.exists(assessment.getId());
        assertThat(assessmentExistsInEs).isFalse();

        // Validate the database is empty
        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAssessment() throws Exception {
        // Initialize the database
        assessmentService.save(assessment);

        // Search the assessment
        restAssessmentMockMvc.perform(get("/api/_search/assessments?query=id:" + assessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationMoment").value(hasItem(DEFAULT_CREATION_MOMENT_STR)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
}
