package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.NaturalPersonForm;
import com.hielfsoft.volunteercrowd.repository.NaturalPersonFormRepository;
import com.hielfsoft.volunteercrowd.repository.search.NaturalPersonFormSearchRepository;
import com.hielfsoft.volunteercrowd.service.NaturalPersonFormService;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the NaturalPersonFormResource REST controller.
 *
 * @see NaturalPersonFormResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class NaturalPersonFormResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BIRTH_DATE_STR = dateTimeFormatter.format(DEFAULT_BIRTH_DATE);
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_ZIP_CODE = "AAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBB";
    private static final String DEFAULT_PROVINCE = "AAAAA";
    private static final String UPDATED_PROVINCE = "BBBBB";
    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "";
    private static final String UPDATED_FIRST_NAME = "";
    private static final String DEFAULT_LAST_NAME = "";
    private static final String UPDATED_LAST_NAME = "";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";
    private static final String DEFAULT_PASSWORD_CONFIRM = "AAAAA";
    private static final String UPDATED_PASSWORD_CONFIRM = "BBBBB";
    private static final String DEFAULT_LOGIN = "A";
    private static final String UPDATED_LOGIN = "B";

    private static final Boolean DEFAULT_ACCEPT_TERMS_AND_CONDITIONS = false;
    private static final Boolean UPDATED_ACCEPT_TERMS_AND_CONDITIONS = true;

    @Inject
    private NaturalPersonFormRepository naturalPersonFormRepository;

    @Inject
    private NaturalPersonFormService naturalPersonFormService;

    @Inject
    private NaturalPersonFormSearchRepository naturalPersonFormSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNaturalPersonFormMockMvc;

    private NaturalPersonForm naturalPersonForm;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NaturalPersonFormResource naturalPersonFormResource = new NaturalPersonFormResource();
        ReflectionTestUtils.setField(naturalPersonFormResource, "naturalPersonFormService", naturalPersonFormService);
        this.restNaturalPersonFormMockMvc = MockMvcBuilders.standaloneSetup(naturalPersonFormResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        naturalPersonFormSearchRepository.deleteAll();
        naturalPersonForm = new NaturalPersonForm();
        naturalPersonForm.setBirthDate(DEFAULT_BIRTH_DATE);
        naturalPersonForm.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        naturalPersonForm.setImage(DEFAULT_IMAGE);
        naturalPersonForm.setImageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        naturalPersonForm.setAddress(DEFAULT_ADDRESS);
        naturalPersonForm.setCity(DEFAULT_CITY);
        naturalPersonForm.setZipCode(DEFAULT_ZIP_CODE);
        naturalPersonForm.setProvince(DEFAULT_PROVINCE);
        naturalPersonForm.setCountry(DEFAULT_COUNTRY);
        naturalPersonForm.setFirstName(DEFAULT_FIRST_NAME);
        naturalPersonForm.setLastName(DEFAULT_LAST_NAME);
        naturalPersonForm.setEmail(DEFAULT_EMAIL);
        naturalPersonForm.setPassword(DEFAULT_PASSWORD);
        naturalPersonForm.setPasswordConfirm(DEFAULT_PASSWORD_CONFIRM);
        naturalPersonForm.setLogin(DEFAULT_LOGIN);
        naturalPersonForm.setAcceptTermsAndConditions(DEFAULT_ACCEPT_TERMS_AND_CONDITIONS);
    }

    @Test
    @Transactional
    public void createNaturalPersonForm() throws Exception {
        int databaseSizeBeforeCreate = naturalPersonFormRepository.findAll().size();

        // Create the NaturalPersonForm

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isCreated());

        // Validate the NaturalPersonForm in the database
        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeCreate + 1);
        NaturalPersonForm testNaturalPersonForm = naturalPersonForms.get(naturalPersonForms.size() - 1);
        assertThat(testNaturalPersonForm.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testNaturalPersonForm.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testNaturalPersonForm.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testNaturalPersonForm.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testNaturalPersonForm.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testNaturalPersonForm.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testNaturalPersonForm.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testNaturalPersonForm.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testNaturalPersonForm.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testNaturalPersonForm.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testNaturalPersonForm.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testNaturalPersonForm.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNaturalPersonForm.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testNaturalPersonForm.getPasswordConfirm()).isEqualTo(DEFAULT_PASSWORD_CONFIRM);
        assertThat(testNaturalPersonForm.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testNaturalPersonForm.isAcceptTermsAndConditions()).isEqualTo(DEFAULT_ACCEPT_TERMS_AND_CONDITIONS);

        // Validate the NaturalPersonForm in ElasticSearch
        NaturalPersonForm naturalPersonFormEs = naturalPersonFormSearchRepository.findOne(testNaturalPersonForm.getId());
        assertThat(naturalPersonFormEs).isEqualToComparingFieldByField(testNaturalPersonForm);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setBirthDate(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setPhoneNumber(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setCity(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setZipCode(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setProvince(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setCountry(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setFirstName(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setLastName(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setEmail(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setPassword(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordConfirmIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setPasswordConfirm(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setLogin(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcceptTermsAndConditionsIsRequired() throws Exception {
        int databaseSizeBeforeTest = naturalPersonFormRepository.findAll().size();
        // set the field null
        naturalPersonForm.setAcceptTermsAndConditions(null);

        // Create the NaturalPersonForm, which fails.

        restNaturalPersonFormMockMvc.perform(post("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(naturalPersonForm)))
            .andExpect(status().isBadRequest());

        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNaturalPersonForms() throws Exception {
        // Initialize the database
        naturalPersonFormRepository.saveAndFlush(naturalPersonForm);

        // Get all the naturalPersonForms
        restNaturalPersonFormMockMvc.perform(get("/api/natural-person-forms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naturalPersonForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].passwordConfirm").value(hasItem(DEFAULT_PASSWORD_CONFIRM.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].acceptTermsAndConditions").value(hasItem(DEFAULT_ACCEPT_TERMS_AND_CONDITIONS.booleanValue())));
    }

    @Test
    @Transactional
    public void getNaturalPersonForm() throws Exception {
        // Initialize the database
        naturalPersonFormRepository.saveAndFlush(naturalPersonForm);

        // Get the naturalPersonForm
        restNaturalPersonFormMockMvc.perform(get("/api/natural-person-forms/{id}", naturalPersonForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(naturalPersonForm.getId().intValue()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE_STR))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.passwordConfirm").value(DEFAULT_PASSWORD_CONFIRM.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.acceptTermsAndConditions").value(DEFAULT_ACCEPT_TERMS_AND_CONDITIONS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNaturalPersonForm() throws Exception {
        // Get the naturalPersonForm
        restNaturalPersonFormMockMvc.perform(get("/api/natural-person-forms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNaturalPersonForm() throws Exception {
        // Initialize the database
        naturalPersonFormService.save(naturalPersonForm);

        int databaseSizeBeforeUpdate = naturalPersonFormRepository.findAll().size();

        // Update the naturalPersonForm
        NaturalPersonForm updatedNaturalPersonForm = new NaturalPersonForm();
        updatedNaturalPersonForm.setId(naturalPersonForm.getId());
        updatedNaturalPersonForm.setBirthDate(UPDATED_BIRTH_DATE);
        updatedNaturalPersonForm.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedNaturalPersonForm.setImage(UPDATED_IMAGE);
        updatedNaturalPersonForm.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        updatedNaturalPersonForm.setAddress(UPDATED_ADDRESS);
        updatedNaturalPersonForm.setCity(UPDATED_CITY);
        updatedNaturalPersonForm.setZipCode(UPDATED_ZIP_CODE);
        updatedNaturalPersonForm.setProvince(UPDATED_PROVINCE);
        updatedNaturalPersonForm.setCountry(UPDATED_COUNTRY);
        updatedNaturalPersonForm.setFirstName(UPDATED_FIRST_NAME);
        updatedNaturalPersonForm.setLastName(UPDATED_LAST_NAME);
        updatedNaturalPersonForm.setEmail(UPDATED_EMAIL);
        updatedNaturalPersonForm.setPassword(UPDATED_PASSWORD);
        updatedNaturalPersonForm.setPasswordConfirm(UPDATED_PASSWORD_CONFIRM);
        updatedNaturalPersonForm.setLogin(UPDATED_LOGIN);
        updatedNaturalPersonForm.setAcceptTermsAndConditions(UPDATED_ACCEPT_TERMS_AND_CONDITIONS);

        restNaturalPersonFormMockMvc.perform(put("/api/natural-person-forms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNaturalPersonForm)))
            .andExpect(status().isOk());

        // Validate the NaturalPersonForm in the database
        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeUpdate);
        NaturalPersonForm testNaturalPersonForm = naturalPersonForms.get(naturalPersonForms.size() - 1);
        assertThat(testNaturalPersonForm.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testNaturalPersonForm.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testNaturalPersonForm.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testNaturalPersonForm.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testNaturalPersonForm.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testNaturalPersonForm.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testNaturalPersonForm.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testNaturalPersonForm.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testNaturalPersonForm.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testNaturalPersonForm.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testNaturalPersonForm.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testNaturalPersonForm.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNaturalPersonForm.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testNaturalPersonForm.getPasswordConfirm()).isEqualTo(UPDATED_PASSWORD_CONFIRM);
        assertThat(testNaturalPersonForm.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testNaturalPersonForm.isAcceptTermsAndConditions()).isEqualTo(UPDATED_ACCEPT_TERMS_AND_CONDITIONS);

        // Validate the NaturalPersonForm in ElasticSearch
        NaturalPersonForm naturalPersonFormEs = naturalPersonFormSearchRepository.findOne(testNaturalPersonForm.getId());
        assertThat(naturalPersonFormEs).isEqualToComparingFieldByField(testNaturalPersonForm);
    }

    @Test
    @Transactional
    public void deleteNaturalPersonForm() throws Exception {
        // Initialize the database
        naturalPersonFormService.save(naturalPersonForm);

        int databaseSizeBeforeDelete = naturalPersonFormRepository.findAll().size();

        // Get the naturalPersonForm
        restNaturalPersonFormMockMvc.perform(delete("/api/natural-person-forms/{id}", naturalPersonForm.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean naturalPersonFormExistsInEs = naturalPersonFormSearchRepository.exists(naturalPersonForm.getId());
        assertThat(naturalPersonFormExistsInEs).isFalse();

        // Validate the database is empty
        List<NaturalPersonForm> naturalPersonForms = naturalPersonFormRepository.findAll();
        assertThat(naturalPersonForms).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNaturalPersonForm() throws Exception {
        // Initialize the database
        naturalPersonFormService.save(naturalPersonForm);

        // Search the naturalPersonForm
        restNaturalPersonFormMockMvc.perform(get("/api/_search/natural-person-forms?query=id:" + naturalPersonForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naturalPersonForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].passwordConfirm").value(hasItem(DEFAULT_PASSWORD_CONFIRM.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].acceptTermsAndConditions").value(hasItem(DEFAULT_ACCEPT_TERMS_AND_CONDITIONS.booleanValue())));
    }
}
