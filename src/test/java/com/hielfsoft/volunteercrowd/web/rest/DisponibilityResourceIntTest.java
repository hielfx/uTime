package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.Disponibility;
import com.hielfsoft.volunteercrowd.repository.DisponibilityRepository;
import com.hielfsoft.volunteercrowd.service.DisponibilityService;

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
 * Test class for the DisponibilityResource REST controller.
 *
 * @see DisponibilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DisponibilityResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_START_MOMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_MOMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_MOMENT_STR = dateTimeFormatter.format(DEFAULT_START_MOMENT);

    private static final ZonedDateTime DEFAULT_END_MOMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_MOMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_MOMENT_STR = dateTimeFormatter.format(DEFAULT_END_MOMENT);

    @Inject
    private DisponibilityRepository disponibilityRepository;

    @Inject
    private DisponibilityService disponibilityService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDisponibilityMockMvc;

    private Disponibility disponibility;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DisponibilityResource disponibilityResource = new DisponibilityResource();
        ReflectionTestUtils.setField(disponibilityResource, "disponibilityService", disponibilityService);
        this.restDisponibilityMockMvc = MockMvcBuilders.standaloneSetup(disponibilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        disponibility = new Disponibility();
        disponibility.setStartMoment(DEFAULT_START_MOMENT);
        disponibility.setEndMoment(DEFAULT_END_MOMENT);
    }

    @Test
    @Transactional
    public void createDisponibility() throws Exception {
        int databaseSizeBeforeCreate = disponibilityRepository.findAll().size();

        // Create the Disponibility

        restDisponibilityMockMvc.perform(post("/api/disponibilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibility)))
                .andExpect(status().isCreated());

        // Validate the Disponibility in the database
        List<Disponibility> disponibilitys = disponibilityRepository.findAll();
        assertThat(disponibilitys).hasSize(databaseSizeBeforeCreate + 1);
        Disponibility testDisponibility = disponibilitys.get(disponibilitys.size() - 1);
        assertThat(testDisponibility.getStartMoment()).isEqualTo(DEFAULT_START_MOMENT);
        assertThat(testDisponibility.getEndMoment()).isEqualTo(DEFAULT_END_MOMENT);
    }

    @Test
    @Transactional
    public void checkStartMomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilityRepository.findAll().size();
        // set the field null
        disponibility.setStartMoment(null);

        // Create the Disponibility, which fails.

        restDisponibilityMockMvc.perform(post("/api/disponibilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibility)))
                .andExpect(status().isBadRequest());

        List<Disponibility> disponibilitys = disponibilityRepository.findAll();
        assertThat(disponibilitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndMomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = disponibilityRepository.findAll().size();
        // set the field null
        disponibility.setEndMoment(null);

        // Create the Disponibility, which fails.

        restDisponibilityMockMvc.perform(post("/api/disponibilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibility)))
                .andExpect(status().isBadRequest());

        List<Disponibility> disponibilitys = disponibilityRepository.findAll();
        assertThat(disponibilitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDisponibilitys() throws Exception {
        // Initialize the database
        disponibilityRepository.saveAndFlush(disponibility);

        // Get all the disponibilitys
        restDisponibilityMockMvc.perform(get("/api/disponibilitys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(disponibility.getId().intValue())))
                .andExpect(jsonPath("$.[*].startMoment").value(hasItem(DEFAULT_START_MOMENT_STR)))
                .andExpect(jsonPath("$.[*].endMoment").value(hasItem(DEFAULT_END_MOMENT_STR)));
    }

    @Test
    @Transactional
    public void getDisponibility() throws Exception {
        // Initialize the database
        disponibilityRepository.saveAndFlush(disponibility);

        // Get the disponibility
        restDisponibilityMockMvc.perform(get("/api/disponibilitys/{id}", disponibility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(disponibility.getId().intValue()))
            .andExpect(jsonPath("$.startMoment").value(DEFAULT_START_MOMENT_STR))
            .andExpect(jsonPath("$.endMoment").value(DEFAULT_END_MOMENT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingDisponibility() throws Exception {
        // Get the disponibility
        restDisponibilityMockMvc.perform(get("/api/disponibilitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisponibility() throws Exception {
        // Initialize the database
        disponibilityRepository.saveAndFlush(disponibility);

		int databaseSizeBeforeUpdate = disponibilityRepository.findAll().size();

        // Update the disponibility
        disponibility.setStartMoment(UPDATED_START_MOMENT);
        disponibility.setEndMoment(UPDATED_END_MOMENT);

        restDisponibilityMockMvc.perform(put("/api/disponibilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(disponibility)))
                .andExpect(status().isOk());

        // Validate the Disponibility in the database
        List<Disponibility> disponibilitys = disponibilityRepository.findAll();
        assertThat(disponibilitys).hasSize(databaseSizeBeforeUpdate);
        Disponibility testDisponibility = disponibilitys.get(disponibilitys.size() - 1);
        assertThat(testDisponibility.getStartMoment()).isEqualTo(UPDATED_START_MOMENT);
        assertThat(testDisponibility.getEndMoment()).isEqualTo(UPDATED_END_MOMENT);
    }

    @Test
    @Transactional
    public void deleteDisponibility() throws Exception {
        // Initialize the database
        disponibilityRepository.saveAndFlush(disponibility);

		int databaseSizeBeforeDelete = disponibilityRepository.findAll().size();

        // Get the disponibility
        restDisponibilityMockMvc.perform(delete("/api/disponibilitys/{id}", disponibility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Disponibility> disponibilitys = disponibilityRepository.findAll();
        assertThat(disponibilitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
