package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Availability;
import com.hielfsoft.volunteercrowd.repository.AvailabilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.AvailabilitySearchRepository;
import com.hielfsoft.volunteercrowd.service.AvailabilityService;
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
 * Test class for the AvailabilityResource REST controller.
 *
 * @see AvailabilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class AvailabilityResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_START_MOMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_MOMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_MOMENT_STR = dateTimeFormatter.format(DEFAULT_START_MOMENT);

    private static final ZonedDateTime DEFAULT_END_MOMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_MOMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_MOMENT_STR = dateTimeFormatter.format(DEFAULT_END_MOMENT);

    @Inject
    private AvailabilityRepository availabilityRepository;

    @Inject
    private AvailabilityService availabilityService;

    @Inject
    private AvailabilitySearchRepository availabilitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAvailabilityMockMvc;

    private Availability availability;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvailabilityResource availabilityResource = new AvailabilityResource();
        ReflectionTestUtils.setField(availabilityResource, "availabilityService", availabilityService);
        this.restAvailabilityMockMvc = MockMvcBuilders.standaloneSetup(availabilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        availabilitySearchRepository.deleteAll();
        availability = new Availability();
        availability.setStartMoment(DEFAULT_START_MOMENT);
        availability.setEndMoment(DEFAULT_END_MOMENT);
    }

    @Test
    @Transactional
    public void createAvailability() throws Exception {
        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();

        // Create the Availability

        restAvailabilityMockMvc.perform(post("/api/availabilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availability)))
                .andExpect(status().isCreated());

        // Validate the Availability in the database
        List<Availability> availabilities = availabilityRepository.findAll();
        assertThat(availabilities).hasSize(databaseSizeBeforeCreate + 1);
        Availability testAvailability = availabilities.get(availabilities.size() - 1);
        assertThat(testAvailability.getStartMoment()).isEqualTo(DEFAULT_START_MOMENT);
        assertThat(testAvailability.getEndMoment()).isEqualTo(DEFAULT_END_MOMENT);

        // Validate the Availability in ElasticSearch
        Availability availabilityEs = availabilitySearchRepository.findOne(testAvailability.getId());
        assertThat(availabilityEs).isEqualToComparingFieldByField(testAvailability);
    }

    @Test
    @Transactional
    public void checkStartMomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setStartMoment(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc.perform(post("/api/availabilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availability)))
                .andExpect(status().isBadRequest());

        List<Availability> availabilities = availabilityRepository.findAll();
        assertThat(availabilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndMomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = availabilityRepository.findAll().size();
        // set the field null
        availability.setEndMoment(null);

        // Create the Availability, which fails.

        restAvailabilityMockMvc.perform(post("/api/availabilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availability)))
                .andExpect(status().isBadRequest());

        List<Availability> availabilities = availabilityRepository.findAll();
        assertThat(availabilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAvailabilities() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilities
        restAvailabilityMockMvc.perform(get("/api/availabilities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
                .andExpect(jsonPath("$.[*].startMoment").value(hasItem(DEFAULT_START_MOMENT_STR)))
                .andExpect(jsonPath("$.[*].endMoment").value(hasItem(DEFAULT_END_MOMENT_STR)));
    }

    @Test
    @Transactional
    public void getAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get the availability
        restAvailabilityMockMvc.perform(get("/api/availabilities/{id}", availability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(availability.getId().intValue()))
            .andExpect(jsonPath("$.startMoment").value(DEFAULT_START_MOMENT_STR))
            .andExpect(jsonPath("$.endMoment").value(DEFAULT_END_MOMENT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAvailability() throws Exception {
        // Get the availability
        restAvailabilityMockMvc.perform(get("/api/availabilities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailability() throws Exception {
        // Initialize the database
        availabilityService.save(availability);

        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability
        Availability updatedAvailability = new Availability();
        updatedAvailability.setId(availability.getId());
        updatedAvailability.setStartMoment(UPDATED_START_MOMENT);
        updatedAvailability.setEndMoment(UPDATED_END_MOMENT);

        restAvailabilityMockMvc.perform(put("/api/availabilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvailability)))
                .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilities = availabilityRepository.findAll();
        assertThat(availabilities).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilities.get(availabilities.size() - 1);
        assertThat(testAvailability.getStartMoment()).isEqualTo(UPDATED_START_MOMENT);
        assertThat(testAvailability.getEndMoment()).isEqualTo(UPDATED_END_MOMENT);

        // Validate the Availability in ElasticSearch
        Availability availabilityEs = availabilitySearchRepository.findOne(testAvailability.getId());
        assertThat(availabilityEs).isEqualToComparingFieldByField(testAvailability);
    }

    @Test
    @Transactional
    public void deleteAvailability() throws Exception {
        // Initialize the database
        availabilityService.save(availability);

        int databaseSizeBeforeDelete = availabilityRepository.findAll().size();

        // Get the availability
        restAvailabilityMockMvc.perform(delete("/api/availabilities/{id}", availability.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean availabilityExistsInEs = availabilitySearchRepository.exists(availability.getId());
        assertThat(availabilityExistsInEs).isFalse();

        // Validate the database is empty
        List<Availability> availabilities = availabilityRepository.findAll();
        assertThat(availabilities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAvailability() throws Exception {
        // Initialize the database
        availabilityService.save(availability);

        // Search the availability
        restAvailabilityMockMvc.perform(get("/api/_search/availabilities?query=id:" + availability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
            .andExpect(jsonPath("$.[*].startMoment").value(hasItem(DEFAULT_START_MOMENT_STR)))
            .andExpect(jsonPath("$.[*].endMoment").value(hasItem(DEFAULT_END_MOMENT_STR)));
    }
}
