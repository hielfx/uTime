package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.repository.AppUserRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;

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
 * Test class for the AppUserResource REST controller.
 *
 * @see AppUserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppUserResourceIntTest {

    private static final String DEFAULT_PHONE_NOMBER = "AAAAA";
    private static final String UPDATED_PHONE_NOMBER = "BBBBB";

    private static final Boolean DEFAULT_IS_ONLINE = false;
    private static final Boolean UPDATED_IS_ONLINE = true;

    @Inject
    private AppUserRepository appUserRepository;

    @Inject
    private AppUserService appUserService;

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
        appUser = new AppUser();
        appUser.setPhoneNumber(DEFAULT_PHONE_NOMBER);
        appUser.setIsOnline(DEFAULT_IS_ONLINE);
    }

    @Test
    @Transactional
    public void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // Create the AppUser

        restAppUserMockMvc.perform(post("/api/appUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appUser)))
                .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUsers.get(appUsers.size() - 1);
        assertThat(testAppUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NOMBER);
        assertThat(testAppUser.getIsOnline()).isEqualTo(DEFAULT_IS_ONLINE);
    }

    @Test
    @Transactional
    public void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUsers
        restAppUserMockMvc.perform(get("/api/appUsers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NOMBER.toString())))
                .andExpect(jsonPath("$.[*].isOnline").value(hasItem(DEFAULT_IS_ONLINE.booleanValue())));
    }

    @Test
    @Transactional
    public void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc.perform(get("/api/appUsers/{id}", appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NOMBER.toString()))
            .andExpect(jsonPath("$.isOnline").value(DEFAULT_IS_ONLINE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get("/api/appUsers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

		int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        appUser.setPhoneNumber(UPDATED_PHONE_NOMBER);
        appUser.setIsOnline(UPDATED_IS_ONLINE);

        restAppUserMockMvc.perform(put("/api/appUsers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appUser)))
                .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUsers.get(appUsers.size() - 1);
        assertThat(testAppUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NOMBER);
        assertThat(testAppUser.getIsOnline()).isEqualTo(UPDATED_IS_ONLINE);
    }

    @Test
    @Transactional
    public void deleteAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

		int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Get the appUser
        restAppUserMockMvc.perform(delete("/api/appUsers/{id}", appUser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AppUser> appUsers = appUserRepository.findAll();
        assertThat(appUsers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
