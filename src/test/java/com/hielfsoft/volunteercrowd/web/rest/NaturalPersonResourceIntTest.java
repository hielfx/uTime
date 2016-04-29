package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.*;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonSearchRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;
import com.hielfsoft.volunteercrowd.service.NaturalPersonService;
import com.hielfsoft.volunteercrowd.service.UserService;
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
 * Test class for the NaturalPersonResource REST controller.
 *
 * @see NaturalPersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class NaturalPersonResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BIRTH_DATE_STR = dateTimeFormatter.format(DEFAULT_BIRTH_DATE);

    @Inject
    private NaturalPersonRepository naturalPersonRepository;

    @Inject
    private NaturalPersonService naturalPersonService;

    @Inject
    private NaturalPersonSearchRepository naturalPersonSearchRepository;

    @Inject
    private AppUserService appUserService;

    @Inject
    private UserService userService;

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
        ReflectionTestUtils.setField(naturalPersonResource, "naturalPersonService", naturalPersonService);
        this.restNaturalPersonMockMvc = MockMvcBuilders.standaloneSetup(naturalPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        naturalPersonSearchRepository.deleteAll();
        naturalPerson = new NaturalPerson();
        naturalPerson.setBirthDate(DEFAULT_BIRTH_DATE);

        //Create appUser
        AppUser appUser = new AppUser();

        appUser.setIsOnline(false);
        appUser.setTokens(0);
        appUser.setPhoneNumber("698741253");
        appUser.setImage(TestUtil.createByteArray(1, "0"));
        appUser.setImageContentType("image/jpg");


        //Create user and address
        User user;
        Address address = new Address();

        String login = "newappuser";
        String password = "new_password";
        String firstName = "New App User";
        String lastName = "AppU";
        String email = "new_app_user@appuser.com";
        String langKey = "es";

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

        user = userService.createUserInformation(login, password, firstName, lastName, email, langKey);

        appUser.setAddress(address);
        appUser.setUser(user);

        userService.save(user);

        appUserService.save(appUser);

        naturalPerson.setAppUser(appUser);

        Gender gender = new Gender();
        gender.setName(GenderConstants.MALE);
        naturalPerson.setGender(gender);
    }

    @Test
    @Transactional
    public void createNaturalPerson() throws Exception {
        int databaseSizeBeforeCreate = naturalPersonRepository.findAll().size();

        // Create the NaturalPerson

        restNaturalPersonMockMvc.perform(post("/api/natural-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(naturalPerson)))
                .andExpect(status().isCreated());

        // Validate the NaturalPerson in the database
        List<NaturalPerson> naturalPeople = naturalPersonRepository.findAll();
        assertThat(naturalPeople).hasSize(databaseSizeBeforeCreate + 1);
        NaturalPerson testNaturalPerson = naturalPeople.get(naturalPeople.size() - 1);
        assertThat(testNaturalPerson.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);

        // Validate the NaturalPerson in ElasticSearch
        NaturalPerson naturalPersonEs = naturalPersonSearchRepository.findOne(testNaturalPerson.getId());
        assertThat(naturalPersonEs).isEqualToComparingFieldByField(testNaturalPerson);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonRepository.findAll().size();
        // set the field null
        naturalPerson.setBirthDate(null);

        // Create the NaturalPerson, which fails.

        restNaturalPersonMockMvc.perform(post("/api/natural-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(naturalPerson)))
                .andExpect(status().isBadRequest());

        List<NaturalPerson> naturalPeople = naturalPersonRepository.findAll();
        assertThat(naturalPeople).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNaturalPeople() throws Exception {
        // Initialize the database
        naturalPersonRepository.saveAndFlush(naturalPerson);

        // Get all the naturalPeople
        restNaturalPersonMockMvc.perform(get("/api/natural-people?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(naturalPerson.getId().intValue())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)));
    }

    @Test
    @Transactional
    public void getNaturalPerson() throws Exception {
        // Initialize the database
        naturalPersonRepository.saveAndFlush(naturalPerson);

        // Get the naturalPerson
        restNaturalPersonMockMvc.perform(get("/api/natural-people/{id}", naturalPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(naturalPerson.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingNaturalPerson() throws Exception {
        // Get the naturalPerson
        restNaturalPersonMockMvc.perform(get("/api/natural-people/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNaturalPerson() throws Exception {
        // Initialize the database
        naturalPersonService.save(naturalPerson);

        int databaseSizeBeforeUpdate = naturalPersonRepository.findAll().size();

        // Update the naturalPerson
        naturalPerson.setId(naturalPerson.getId());
        naturalPerson.setBirthDate(UPDATED_BIRTH_DATE);

        restNaturalPersonMockMvc.perform(put("/api/natural-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPerson)))
                .andExpect(status().isOk());

        // Validate the NaturalPerson in the database
        List<NaturalPerson> naturalPeople = naturalPersonRepository.findAll();
        assertThat(naturalPeople).hasSize(databaseSizeBeforeUpdate);
        NaturalPerson testNaturalPerson = naturalPeople.get(naturalPeople.size() - 1);
        assertThat(testNaturalPerson.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);

        // Validate the NaturalPerson in ElasticSearch
        NaturalPerson naturalPersonEs = naturalPersonSearchRepository.findOne(testNaturalPerson.getId());
        assertThat(naturalPersonEs).isEqualToComparingFieldByField(testNaturalPerson);
    }

    @Test
    @Transactional
    public void deleteNaturalPerson() throws Exception {
        // Initialize the database
        naturalPersonService.save(naturalPerson);

        int databaseSizeBeforeDelete = naturalPersonRepository.findAll().size();

        // Get the naturalPerson
        restNaturalPersonMockMvc.perform(delete("/api/natural-people/{id}", naturalPerson.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean naturalPersonExistsInEs = naturalPersonSearchRepository.exists(naturalPerson.getId());
        assertThat(naturalPersonExistsInEs).isFalse();

        // Validate the database is empty
        List<NaturalPerson> naturalPeople = naturalPersonRepository.findAll();
        assertThat(naturalPeople).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNaturalPerson() throws Exception {
        // Initialize the database
        naturalPersonService.save(naturalPerson);

        // Search the naturalPerson
        restNaturalPersonMockMvc.perform(get("/api/_search/natural-people?query=id:" + naturalPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naturalPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)));
    }
}
