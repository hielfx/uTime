package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Ability;
import com.hielfsoft.volunteercrowd.repository.AbilityRepository;
import com.hielfsoft.volunteercrowd.repository.search.AbilitySearchRepository;
import com.hielfsoft.volunteercrowd.service.AbilityService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AbilityResource REST controller.
 *
 * @see AbilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class AbilityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    @Inject
    private AbilityRepository abilityRepository;

    @Inject
    private AbilityService abilityService;

    @Inject
    private AbilitySearchRepository abilitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAbilityMockMvc;

    private Ability ability;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AbilityResource abilityResource = new AbilityResource();
        ReflectionTestUtils.setField(abilityResource, "abilityService", abilityService);
        this.restAbilityMockMvc = MockMvcBuilders.standaloneSetup(abilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        abilitySearchRepository.deleteAll();
        ability = new Ability();
        ability.setName(DEFAULT_NAME);
        ability.setHidden(DEFAULT_HIDDEN);
    }

    @Test
    @Transactional
    public void createAbility() throws Exception {
        int databaseSizeBeforeCreate = abilityRepository.findAll().size();

        // Create the Ability

        restAbilityMockMvc.perform(post("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ability)))
                .andExpect(status().isCreated());

        // Validate the Ability in the database
        List<Ability> abilities = abilityRepository.findAll();
        assertThat(abilities).hasSize(databaseSizeBeforeCreate + 1);
        Ability testAbility = abilities.get(abilities.size() - 1);
        assertThat(testAbility.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAbility.isHidden()).isEqualTo(DEFAULT_HIDDEN);

        // Validate the Ability in ElasticSearch
        Ability abilityEs = abilitySearchRepository.findOne(testAbility.getId());
        assertThat(abilityEs).isEqualToComparingFieldByField(testAbility);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = abilityRepository.findAll().size();
        // set the field null
        ability.setName(null);

        // Create the Ability, which fails.

        restAbilityMockMvc.perform(post("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ability)))
                .andExpect(status().isBadRequest());

        List<Ability> abilities = abilityRepository.findAll();
        assertThat(abilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHiddenIsRequired() throws Exception {
        int databaseSizeBeforeTest = abilityRepository.findAll().size();
        // set the field null
        ability.setHidden(null);

        // Create the Ability, which fails.

        restAbilityMockMvc.perform(post("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ability)))
                .andExpect(status().isBadRequest());

        List<Ability> abilities = abilityRepository.findAll();
        assertThat(abilities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbilities() throws Exception {
        // Initialize the database
        abilityRepository.saveAndFlush(ability);

        // Get all the abilities
        restAbilityMockMvc.perform(get("/api/abilities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ability.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())));
    }

    @Test
    @Transactional
    public void getAbility() throws Exception {
        // Initialize the database
        abilityRepository.saveAndFlush(ability);

        // Get the ability
        restAbilityMockMvc.perform(get("/api/abilities/{id}", ability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ability.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAbility() throws Exception {
        // Get the ability
        restAbilityMockMvc.perform(get("/api/abilities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbility() throws Exception {
        // Initialize the database
        abilityService.save(ability);

        int databaseSizeBeforeUpdate = abilityRepository.findAll().size();

        // Update the ability
        Ability updatedAbility = new Ability();
        updatedAbility.setId(ability.getId());
        updatedAbility.setName(UPDATED_NAME);
        updatedAbility.setHidden(UPDATED_HIDDEN);

        restAbilityMockMvc.perform(put("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAbility)))
                .andExpect(status().isOk());

        // Validate the Ability in the database
        List<Ability> abilities = abilityRepository.findAll();
        assertThat(abilities).hasSize(databaseSizeBeforeUpdate);
        Ability testAbility = abilities.get(abilities.size() - 1);
        assertThat(testAbility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAbility.isHidden()).isEqualTo(UPDATED_HIDDEN);

        // Validate the Ability in ElasticSearch
        Ability abilityEs = abilitySearchRepository.findOne(testAbility.getId());
        assertThat(abilityEs).isEqualToComparingFieldByField(testAbility);
    }

    @Test
    @Transactional
    public void deleteAbility() throws Exception {
        // Initialize the database
        abilityService.save(ability);

        int databaseSizeBeforeDelete = abilityRepository.findAll().size();

        // Get the ability
        restAbilityMockMvc.perform(delete("/api/abilities/{id}", ability.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean abilityExistsInEs = abilitySearchRepository.exists(ability.getId());
        assertThat(abilityExistsInEs).isFalse();

        // Validate the database is empty
        List<Ability> abilities = abilityRepository.findAll();
        assertThat(abilities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAbility() throws Exception {
        // Initialize the database
        abilityService.save(ability);

        // Search the ability
        restAbilityMockMvc.perform(get("/api/_search/abilities?query=id:" + ability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ability.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())));
    }
}
