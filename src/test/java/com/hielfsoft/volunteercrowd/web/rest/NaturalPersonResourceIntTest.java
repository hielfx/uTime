package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.*;
import com.hielfsoft.volunteercrowd.repository.AppUserRepository;
import com.hielfsoft.volunteercrowd.repository.GenderRepository;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonSearchRepository;

import com.hielfsoft.volunteercrowd.service.UserService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the NaturalPersonResource REST controller.
 *
 * @see NaturalPersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NaturalPersonResourceIntTest {


    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private UserService userService; //Added manually

    @Inject
    private AppUserRepository appUserRepository; //Added manually

    @Inject
    private GenderRepository genderRepository; //Added manually

    @Inject
    private NaturalPersonRepository naturalPersonRepository;

    @Inject
    private NaturalPersonSearchRepository naturalPersonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNaturalPersonMockMvc;

    private NaturalPerson naturalPerson;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NaturalPersonResource naturalPersonResource = new NaturalPersonResource();
        ReflectionTestUtils.setField(naturalPersonResource, "naturalPersonSearchRepository", naturalPersonSearchRepository);
        ReflectionTestUtils.setField(naturalPersonResource, "naturalPersonRepository", naturalPersonRepository);
        this.restNaturalPersonMockMvc = MockMvcBuilders.standaloneSetup(naturalPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        naturalPerson = new NaturalPerson();
        naturalPerson.setBirthDate(DEFAULT_BIRTH_DATE);
        //Added manually
        AppUser appUser = new AppUser();
        Address address = new Address();
        User user = userService.getUserWithAuthoritiesByLogin("user").get();

        address.setAddress("AAAAAA");
        address.setCity("AAAAAA");
        address.setCountry("AAAAAA");
        address.setProvince("AAAAAAA");
        address.setShowAddress(true);
        address.setShowCity(true);
        address.setZipCode("AAAAAA");
        address.setShowCountry(true);
        address.setShowProvince(true);
        address.setShowZipCode(true);

        appUser.setAddress(address);
        appUser.setFollowers(new ArrayList<AppUser>());
        appUser.setUser(user);
        appUser.setFollowing(new ArrayList<AppUser>());
        appUser.setIsOnline(false);
        appUser.setTokens(0);

        naturalPerson.setGender(genderRepository.findAll().get(0));
        naturalPerson.setAppUser(appUser);
    }

    @Test
    @Transactional
    public void createNaturalPerson() throws Exception {
        int databaseSizeBeforeCreate = naturalPersonRepository.findAll().size();

        appUserRepository.saveAndFlush(naturalPerson.getAppUser());
        // Create the NaturalPerson

        restNaturalPersonMockMvc.perform(post("/api/naturalPersons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(naturalPerson)))
                .andExpect(status().isCreated());

        // Validate the NaturalPerson in the database
        List<NaturalPerson> naturalPersons = naturalPersonRepository.findAll();
        assertThat(naturalPersons).hasSize(databaseSizeBeforeCreate + 1);
        NaturalPerson testNaturalPerson = naturalPersons.get(naturalPersons.size() - 1);
        assertThat(testNaturalPerson.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonRepository.findAll().size();
        // set the field null
        naturalPerson.setBirthDate(null);

        // Create the NaturalPerson, which fails.

        restNaturalPersonMockMvc.perform(post("/api/naturalPersons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(naturalPerson)))
                .andExpect(status().isBadRequest());

        List<NaturalPerson> naturalPersons = naturalPersonRepository.findAll();
        assertThat(naturalPersons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNaturalPersons() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(naturalPerson.getAppUser());
        naturalPersonRepository.saveAndFlush(naturalPerson);

        // Get all the naturalPersons
        restNaturalPersonMockMvc.perform(get("/api/naturalPersons?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(naturalPerson.getId().intValue())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @Test
    @Transactional
    public void getNaturalPerson() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(naturalPerson.getAppUser());
        naturalPersonRepository.saveAndFlush(naturalPerson);

        // Get the naturalPerson
        restNaturalPersonMockMvc.perform(get("/api/naturalPersons/{id}", naturalPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(naturalPerson.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNaturalPerson() throws Exception {
        // Get the naturalPerson
        restNaturalPersonMockMvc.perform(get("/api/naturalPersons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNaturalPerson() throws Exception {
        // Initialize the database

        appUserRepository.saveAndFlush(naturalPerson.getAppUser()); //Added manually
        naturalPersonRepository.saveAndFlush(naturalPerson);

		int databaseSizeBeforeUpdate = naturalPersonRepository.findAll().size();

        // Update the naturalPerson
        naturalPerson.setBirthDate(UPDATED_BIRTH_DATE);

        restNaturalPersonMockMvc.perform(put("/api/naturalPersons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(naturalPerson)))
                .andExpect(status().isOk());

        // Validate the NaturalPerson in the database
        List<NaturalPerson> naturalPersons = naturalPersonRepository.findAll();
        assertThat(naturalPersons).hasSize(databaseSizeBeforeUpdate);
        NaturalPerson testNaturalPerson = naturalPersons.get(naturalPersons.size() - 1);
        assertThat(testNaturalPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void deleteNaturalPerson() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(naturalPerson.getAppUser()); //Added manually
        naturalPersonRepository.saveAndFlush(naturalPerson);

		int databaseSizeBeforeDelete = naturalPersonRepository.findAll().size();

        // Get the naturalPerson
        restNaturalPersonMockMvc.perform(delete("/api/naturalPersons/{id}", naturalPerson.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NaturalPerson> naturalPersons = naturalPersonRepository.findAll();
        assertThat(naturalPersons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
