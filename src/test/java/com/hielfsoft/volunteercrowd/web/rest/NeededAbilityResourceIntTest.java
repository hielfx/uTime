package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.NeededAbility;
import com.hielfsoft.volunteercrowd.repository.NeededAbilityRepository;
import com.hielfsoft.volunteercrowd.service.NeededAbilityService;

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
 * Test class for the NeededAbilityResource REST controller.
 *
 * @see NeededAbilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NeededAbilityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private NeededAbilityRepository neededAbilityRepository;

    @Inject
    private NeededAbilityService neededAbilityService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNeededAbilityMockMvc;

    private NeededAbility neededAbility;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NeededAbilityResource neededAbilityResource = new NeededAbilityResource();
        ReflectionTestUtils.setField(neededAbilityResource, "neededAbilityService", neededAbilityService);
        this.restNeededAbilityMockMvc = MockMvcBuilders.standaloneSetup(neededAbilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        neededAbility = new NeededAbility();
        neededAbility.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createNeededAbility() throws Exception {
        int databaseSizeBeforeCreate = neededAbilityRepository.findAll().size();

        // Create the NeededAbility

        restNeededAbilityMockMvc.perform(post("/api/neededAbilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(neededAbility)))
                .andExpect(status().isCreated());

        // Validate the NeededAbility in the database
        List<NeededAbility> neededAbilitys = neededAbilityRepository.findAll();
        assertThat(neededAbilitys).hasSize(databaseSizeBeforeCreate + 1);
        NeededAbility testNeededAbility = neededAbilitys.get(neededAbilitys.size() - 1);
        assertThat(testNeededAbility.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = neededAbilityRepository.findAll().size();
        // set the field null
        neededAbility.setName(null);

        // Create the NeededAbility, which fails.

        restNeededAbilityMockMvc.perform(post("/api/neededAbilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(neededAbility)))
                .andExpect(status().isBadRequest());

        List<NeededAbility> neededAbilitys = neededAbilityRepository.findAll();
        assertThat(neededAbilitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNeededAbilitys() throws Exception {
        // Initialize the database
        neededAbilityRepository.saveAndFlush(neededAbility);

        // Get all the neededAbilitys
        restNeededAbilityMockMvc.perform(get("/api/neededAbilitys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(neededAbility.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getNeededAbility() throws Exception {
        // Initialize the database
        neededAbilityRepository.saveAndFlush(neededAbility);

        // Get the neededAbility
        restNeededAbilityMockMvc.perform(get("/api/neededAbilitys/{id}", neededAbility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(neededAbility.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNeededAbility() throws Exception {
        // Get the neededAbility
        restNeededAbilityMockMvc.perform(get("/api/neededAbilitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNeededAbility() throws Exception {
        // Initialize the database
        neededAbilityRepository.saveAndFlush(neededAbility);

		int databaseSizeBeforeUpdate = neededAbilityRepository.findAll().size();

        // Update the neededAbility
        neededAbility.setName(UPDATED_NAME);

        restNeededAbilityMockMvc.perform(put("/api/neededAbilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(neededAbility)))
                .andExpect(status().isOk());

        // Validate the NeededAbility in the database
        List<NeededAbility> neededAbilitys = neededAbilityRepository.findAll();
        assertThat(neededAbilitys).hasSize(databaseSizeBeforeUpdate);
        NeededAbility testNeededAbility = neededAbilitys.get(neededAbilitys.size() - 1);
        assertThat(testNeededAbility.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteNeededAbility() throws Exception {
        // Initialize the database
        neededAbilityRepository.saveAndFlush(neededAbility);

		int databaseSizeBeforeDelete = neededAbilityRepository.findAll().size();

        // Get the neededAbility
        restNeededAbilityMockMvc.perform(delete("/api/neededAbilitys/{id}", neededAbility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NeededAbility> neededAbilitys = neededAbilityRepository.findAll();
        assertThat(neededAbilitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
