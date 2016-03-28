package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.Ability;
import com.hielfsoft.volunteercrowd.repository.AbilityRepository;
import com.hielfsoft.volunteercrowd.service.AbilityService;

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
 * Test class for the AbilityResource REST controller.
 *
 * @see AbilityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
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
        ability = new Ability();
        ability.setName(DEFAULT_NAME);
        ability.setHidden(DEFAULT_HIDDEN);
    }

    @Test
    @Transactional
    public void createAbility() throws Exception {
        int databaseSizeBeforeCreate = abilityRepository.findAll().size();

        // Create the Ability

        restAbilityMockMvc.perform(post("/api/abilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ability)))
                .andExpect(status().isCreated());

        // Validate the Ability in the database
        List<Ability> abilitys = abilityRepository.findAll();
        assertThat(abilitys).hasSize(databaseSizeBeforeCreate + 1);
        Ability testAbility = abilitys.get(abilitys.size() - 1);
        assertThat(testAbility.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAbility.getHidden()).isEqualTo(DEFAULT_HIDDEN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = abilityRepository.findAll().size();
        // set the field null
        ability.setName(null);

        // Create the Ability, which fails.

        restAbilityMockMvc.perform(post("/api/abilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ability)))
                .andExpect(status().isBadRequest());

        List<Ability> abilitys = abilityRepository.findAll();
        assertThat(abilitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbilitys() throws Exception {
        // Initialize the database
        abilityRepository.saveAndFlush(ability);

        // Get all the abilitys
        restAbilityMockMvc.perform(get("/api/abilitys?sort=id,desc"))
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
        restAbilityMockMvc.perform(get("/api/abilitys/{id}", ability.getId()))
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
        restAbilityMockMvc.perform(get("/api/abilitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbility() throws Exception {
        // Initialize the database
        abilityRepository.saveAndFlush(ability);

		int databaseSizeBeforeUpdate = abilityRepository.findAll().size();

        // Update the ability
        ability.setName(UPDATED_NAME);
        ability.setHidden(UPDATED_HIDDEN);

        restAbilityMockMvc.perform(put("/api/abilitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ability)))
                .andExpect(status().isOk());

        // Validate the Ability in the database
        List<Ability> abilitys = abilityRepository.findAll();
        assertThat(abilitys).hasSize(databaseSizeBeforeUpdate);
        Ability testAbility = abilitys.get(abilitys.size() - 1);
        assertThat(testAbility.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAbility.getHidden()).isEqualTo(UPDATED_HIDDEN);
    }

    @Test
    @Transactional
    public void deleteAbility() throws Exception {
        // Initialize the database
        abilityRepository.saveAndFlush(ability);

		int databaseSizeBeforeDelete = abilityRepository.findAll().size();

        // Get the ability
        restAbilityMockMvc.perform(delete("/api/abilitys/{id}", ability.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ability> abilitys = abilityRepository.findAll();
        assertThat(abilitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
