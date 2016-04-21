package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.repository.AppUserRepository;
import com.hielfsoft.volunteercrowd.repository.search.AppUserSearchRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AppUserResource REST controller.
 *
 * @see AppUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class AppUserResourceIntTest {

    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final Boolean DEFAULT_IS_ONLINE = false;
    private static final Boolean UPDATED_IS_ONLINE = true;

    private static final Integer DEFAULT_TOKENS = 0;
    private static final Integer UPDATED_TOKENS = 1;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private AppUserRepository appUserRepository;

    @Inject
    private AppUserService appUserService;

    @Inject
    private AppUserSearchRepository appUserSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppUserResource appUserResource = new AppUserResource();
        ReflectionTestUtils.setField(appUserResource, "appUserService", appUserService);
        this.restAppUserMockMvc = MockMvcBuilders.standaloneSetup(appUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appUserSearchRepository.deleteAll();
        appUser = new AppUser();
        appUser.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        appUser.setIsOnline(DEFAULT_IS_ONLINE);
        appUser.setTokens(DEFAULT_TOKENS);
        appUser.setImage(DEFAULT_IMAGE);
        appUser.setImageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // Create the AppUser

        restAppUserMockMvc.perform(post("/api/app-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appUser)))
                .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUsers.get(appUsers.size() - 1);
        assertThat(testAppUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAppUser.isIsOnline()).isEqualTo(DEFAULT_IS_ONLINE);
        assertThat(testAppUser.getTokens()).isEqualTo(DEFAULT_TOKENS);
        assertThat(testAppUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAppUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the AppUser in ElasticSearch
        AppUser appUserEs = appUserSearchRepository.findOne(testAppUser.getId());
        assertThat(appUserEs).isEqualToComparingFieldByField(testAppUser);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setPhoneNumber(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc.perform(post("/api/app-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appUser)))
                .andExpect(status().isBadRequest());

        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsOnlineIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setIsOnline(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc.perform(post("/api/app-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appUser)))
                .andExpect(status().isBadRequest());

        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTokensIsRequired() throws Exception {
        int databaseSizeBeforeTest = appUserRepository.findAll().size();
        // set the field null
        appUser.setTokens(null);

        // Create the AppUser, which fails.

        restAppUserMockMvc.perform(post("/api/app-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appUser)))
                .andExpect(status().isBadRequest());

        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUsers
        restAppUserMockMvc.perform(get("/api/app-users?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].isOnline").value(hasItem(DEFAULT_IS_ONLINE.booleanValue())))
                .andExpect(jsonPath("$.[*].tokens").value(hasItem(DEFAULT_TOKENS)))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc.perform(get("/api/app-users/{id}", appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.isOnline").value(DEFAULT_IS_ONLINE.booleanValue()))
            .andExpect(jsonPath("$.tokens").value(DEFAULT_TOKENS))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get("/api/app-users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppUser() throws Exception {
        // Initialize the database
        appUserService.save(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = new AppUser();
        updatedAppUser.setId(appUser.getId());
        updatedAppUser.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedAppUser.setIsOnline(UPDATED_IS_ONLINE);
        updatedAppUser.setTokens(UPDATED_TOKENS);
        updatedAppUser.setImage(UPDATED_IMAGE);
        updatedAppUser.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restAppUserMockMvc.perform(put("/api/app-users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAppUser)))
                .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUsers.get(appUsers.size() - 1);
        assertThat(testAppUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAppUser.isIsOnline()).isEqualTo(UPDATED_IS_ONLINE);
        assertThat(testAppUser.getTokens()).isEqualTo(UPDATED_TOKENS);
        assertThat(testAppUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the AppUser in ElasticSearch
        AppUser appUserEs = appUserSearchRepository.findOne(testAppUser.getId());
        assertThat(appUserEs).isEqualToComparingFieldByField(testAppUser);
    }

    @Test
    @Transactional
    public void deleteAppUser() throws Exception {
        // Initialize the database
        appUserService.save(appUser);

        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Get the appUser
        restAppUserMockMvc.perform(delete("/api/app-users/{id}", appUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean appUserExistsInEs = appUserSearchRepository.exists(appUser.getId());
        assertThat(appUserExistsInEs).isFalse();

        // Validate the database is empty
        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAppUser() throws Exception {
        // Initialize the database
        appUserService.save(appUser);

        // Search the appUser
        restAppUserMockMvc.perform(get("/api/_search/app-users?query=id:" + appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].isOnline").value(hasItem(DEFAULT_IS_ONLINE.booleanValue())))
            .andExpect(jsonPath("$.[*].tokens").value(hasItem(DEFAULT_TOKENS)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
}
