package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Administrator;
import com.hielfsoft.volunteercrowd.domain.Authority;
import com.hielfsoft.volunteercrowd.domain.User;
import com.hielfsoft.volunteercrowd.repository.AdministratorRepository;
import com.hielfsoft.volunteercrowd.repository.search.AdministratorSearchRepository;
import com.hielfsoft.volunteercrowd.security.AuthoritiesConstants;
import com.hielfsoft.volunteercrowd.service.AdministratorService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AdministratorResource REST controller.
 *
 * @see AdministratorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class AdministratorResourceIntTest {


    @Inject
    private AdministratorRepository administratorRepository;

    @Inject
    private AdministratorService administratorService;

    @Inject
    private AdministratorSearchRepository administratorSearchRepository;

    @Inject
    private UserService userService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdministratorMockMvc;

    private Administrator administrator;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdministratorResource administratorResource = new AdministratorResource();
        ReflectionTestUtils.setField(administratorResource, "administratorService", administratorService);
        this.restAdministratorMockMvc = MockMvcBuilders.standaloneSetup(administratorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        administratorSearchRepository.deleteAll();
        administrator = new Administrator();

        //Create user
        User user;
        String login = "newadmin";
        String password = "new_password";
        String firstName = "New Admin";
        String lastName = "Adm";
        String email = "new_admin@admins.com";
        String langKey = "es";
        Set<Authority> authorities = new HashSet<>();//TODO: Change with create method

        Authority a = new Authority();
        a.setName(AuthoritiesConstants.ADMIN);
        authorities.add(a);
        a = new Authority();
        a.setName(AuthoritiesConstants.USER);
        authorities.add(a);

        user = userService.createUserInformation(login,password,firstName,lastName,email,langKey);
        user.setActivated(true);
        user.setAuthorities(authorities);

        userService.save(user);

        administrator.setUser(user);
    }

    @Test
    @Transactional
    public void createAdministrator() throws Exception {
        int databaseSizeBeforeCreate = administratorRepository.findAll().size();

        // Create the Administrator

        restAdministratorMockMvc.perform(post("/api/administrators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(administrator)))
                .andExpect(status().isCreated());

        // Validate the Administrator in the database
        List<Administrator> administrators = administratorRepository.findAll();
        assertThat(administrators).hasSize(databaseSizeBeforeCreate + 1);
        Administrator testAdministrator = administrators.get(administrators.size() - 1);

        // Validate the Administrator in ElasticSearch
        Administrator administratorEs = administratorSearchRepository.findOne(testAdministrator.getId());
        assertThat(administratorEs).isEqualToComparingFieldByField(testAdministrator);
    }

    @Test
    @Transactional
    public void getAllAdministrators() throws Exception {
        // Initialize the database
        userService.save(administrator.getUser());
        administratorRepository.saveAndFlush(administrator);

        // Get all the administrators
        restAdministratorMockMvc.perform(get("/api/administrators?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(administrator.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get the administrator
        restAdministratorMockMvc.perform(get("/api/administrators/{id}", administrator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(administrator.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdministrator() throws Exception {
        // Get the administrator
        restAdministratorMockMvc.perform(get("/api/administrators/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdministrator() throws Exception {
        // Initialize the database
        administratorService.save(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator
//        Administrator updatedAdministrator = new Administrator();
//        updatedAdministrator.setId(administrator.getId());

        administrator.getUser().setEmail("newemail@new.com");
        User user = userService.save(administrator.getUser());
        administrator.setUser(user);


        restAdministratorMockMvc.perform(put("/api/administrators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(/*updatedAdministrator*/administrator)))
                .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administrators = administratorRepository.findAll();
        assertThat(administrators).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administrators.get(administrators.size() - 1);

        // Validate the Administrator in ElasticSearch
        Administrator administratorEs = administratorSearchRepository.findOne(testAdministrator.getId());
        assertThat(administratorEs).isEqualToComparingFieldByField(testAdministrator);
    }

    @Test
    @Transactional
    public void deleteAdministrator() throws Exception {
        // Initialize the database
        administratorService.save(administrator);

        int databaseSizeBeforeDelete = administratorRepository.findAll().size();

        // Get the administrator
        restAdministratorMockMvc.perform(delete("/api/administrators/{id}", administrator.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean administratorExistsInEs = administratorSearchRepository.exists(administrator.getId());
        assertThat(administratorExistsInEs).isFalse();

        // Validate the database is empty
        List<Administrator> administrators = administratorRepository.findAll();
        assertThat(administrators).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAdministrator() throws Exception {
        // Initialize the database
        administratorService.save(administrator);

        // Search the administrator
        restAdministratorMockMvc.perform(get("/api/_search/administrators?query=id:" + administrator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrator.getId().intValue())));
    }
}
