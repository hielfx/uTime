package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.Address;
import com.hielfsoft.volunteercrowd.domain.AppUser;
import com.hielfsoft.volunteercrowd.domain.LegalEntity;
import com.hielfsoft.volunteercrowd.domain.User;
import com.hielfsoft.volunteercrowd.repository.AppUserRepository;
import com.hielfsoft.volunteercrowd.repository.LegalEntityRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;
import com.hielfsoft.volunteercrowd.service.LegalEntityService;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LegalEntityResource REST controller.
 *
 * @see LegalEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LegalEntityResourceIntTest {

    private static final String DEFAULT_MISSION = "AAAAA";
    private static final String UPDATED_MISSION = "BBBBB";
    private static final String DEFAULT_VISION = "AAAAA";
    private static final String UPDATED_VISION = "BBBBB";
    private static final String DEFAULT_WEBSITE = "http://www.google.es";
    private static final String UPDATED_WEBSITE = "http://www.google.com";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private LegalEntityRepository legalEntityRepository;

    @Inject
    private LegalEntityService legalEntityService;

    @Inject
    private UserService userService;

    @Inject //TODO: Delete Repository and use service
    private AppUserRepository appUserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLegalEntityMockMvc;

    private LegalEntity legalEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LegalEntityResource legalEntityResource = new LegalEntityResource();
        ReflectionTestUtils.setField(legalEntityResource, "legalEntityService", legalEntityService);
        this.restLegalEntityMockMvc = MockMvcBuilders.standaloneSetup(legalEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        legalEntity = new LegalEntity();
        legalEntity.setMission(DEFAULT_MISSION);
        legalEntity.setVision(DEFAULT_VISION);
        legalEntity.setWebsite(DEFAULT_WEBSITE);
        legalEntity.setDescription(DEFAULT_DESCRIPTION);
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

        legalEntity.setAppUser(appUser);
    }

    @Test
    @Transactional
    public void createLegalEntity() throws Exception {
        int databaseSizeBeforeCreate = legalEntityRepository.findAll().size();

        appUserRepository.saveAndFlush(legalEntity.getAppUser()); //Added manually
        // Create the LegalEntity

        restLegalEntityMockMvc.perform(post("/api/legalEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalEntity)))
                .andExpect(status().isCreated());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntitys = legalEntityRepository.findAll();
        assertThat(legalEntitys).hasSize(databaseSizeBeforeCreate + 1);
        LegalEntity testLegalEntity = legalEntitys.get(legalEntitys.size() - 1);
        assertThat(testLegalEntity.getMission()).isEqualTo(DEFAULT_MISSION);
        assertThat(testLegalEntity.getVision()).isEqualTo(DEFAULT_VISION);
        assertThat(testLegalEntity.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testLegalEntity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLegalEntitys() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(legalEntity.getAppUser()); //Added manually
        legalEntityRepository.saveAndFlush(legalEntity);

        // Get all the legalEntitys
        restLegalEntityMockMvc.perform(get("/api/legalEntitys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(legalEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].mission").value(hasItem(DEFAULT_MISSION.toString())))
                .andExpect(jsonPath("$.[*].vision").value(hasItem(DEFAULT_VISION.toString())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLegalEntity() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(legalEntity.getAppUser()); //Added manually
        legalEntityRepository.saveAndFlush(legalEntity);

        // Get the legalEntity
        restLegalEntityMockMvc.perform(get("/api/legalEntitys/{id}", legalEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(legalEntity.getId().intValue()))
            .andExpect(jsonPath("$.mission").value(DEFAULT_MISSION.toString()))
            .andExpect(jsonPath("$.vision").value(DEFAULT_VISION.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLegalEntity() throws Exception {
        // Get the legalEntity
        restLegalEntityMockMvc.perform(get("/api/legalEntitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLegalEntity() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(legalEntity.getAppUser()); //Added manually
        legalEntityRepository.saveAndFlush(legalEntity);

		int databaseSizeBeforeUpdate = legalEntityRepository.findAll().size();

        // Update the legalEntity
        legalEntity.setMission(UPDATED_MISSION);
        legalEntity.setVision(UPDATED_VISION);
        legalEntity.setWebsite(UPDATED_WEBSITE);
        legalEntity.setDescription(UPDATED_DESCRIPTION);

        restLegalEntityMockMvc.perform(put("/api/legalEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(legalEntity)))
                .andExpect(status().isOk());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntitys = legalEntityRepository.findAll();
        assertThat(legalEntitys).hasSize(databaseSizeBeforeUpdate);
        LegalEntity testLegalEntity = legalEntitys.get(legalEntitys.size() - 1);
        assertThat(testLegalEntity.getMission()).isEqualTo(UPDATED_MISSION);
        assertThat(testLegalEntity.getVision()).isEqualTo(UPDATED_VISION);
        assertThat(testLegalEntity.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testLegalEntity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteLegalEntity() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(legalEntity.getAppUser()); //Added manually
        legalEntityRepository.saveAndFlush(legalEntity);

		int databaseSizeBeforeDelete = legalEntityRepository.findAll().size();

        // Get the legalEntity
        restLegalEntityMockMvc.perform(delete("/api/legalEntitys/{id}", legalEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LegalEntity> legalEntitys = legalEntityRepository.findAll();
        assertThat(legalEntitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
