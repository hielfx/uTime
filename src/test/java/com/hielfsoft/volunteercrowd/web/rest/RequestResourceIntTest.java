package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.Application;
import com.hielfsoft.volunteercrowd.domain.Request;
import com.hielfsoft.volunteercrowd.repository.RequestRepository;
import com.hielfsoft.volunteercrowd.repository.RequestStatusRepository;
import com.hielfsoft.volunteercrowd.service.RequestService;

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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RequestResource REST controller.
 *
 * @see RequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RequestResourceIntTest {


    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusYears(1);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_FINISH_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusYears(1);
    private static final ZonedDateTime UPDATED_FINISH_DATE = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private RequestService requestService;

    @Inject //TODO:Delete repository and use service
    private RequestStatusRepository requestStatusRepository; //Added manually

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRequestMockMvc;

    private Request request;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestResource requestResource = new RequestResource();
        ReflectionTestUtils.setField(requestResource, "requestService", requestService);
        this.restRequestMockMvc = MockMvcBuilders.standaloneSetup(requestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        request = new Request();
        request.setCreationDate(DEFAULT_CREATION_DATE);
        request.setDescription(DEFAULT_DESCRIPTION);
        request.setCode(DEFAULT_CODE);
        request.setFinishDate(DEFAULT_FINISH_DATE);
        request.setDeleted(DEFAULT_DELETED);

        //Added manually
        request.setStatus(requestStatusRepository.findAll().get(0));

    }

    @Test
    @Transactional
    public void createRequest() throws Exception {
        int databaseSizeBeforeCreate = requestRepository.findAll().size();

        // Create the Request

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isCreated());

        // Validate the Request in the database
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeCreate + 1);
        Request testRequest = requests.get(requests.size() - 1);
        assertThat(testRequest.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRequest.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testRequest.getFinishDate()).isEqualTo(DEFAULT_FINISH_DATE);
        assertThat(testRequest.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void getAllRequests() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get all the requests
        restRequestMockMvc.perform(get("/api/requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

        // Get the request
        restRequestMockMvc.perform(get("/api/requests/{id}", request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(request.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get("/api/requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

		int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        request.setCreationDate(UPDATED_CREATION_DATE);
        request.setDescription(UPDATED_DESCRIPTION);
        request.setCode(UPDATED_CODE);
        request.setFinishDate(UPDATED_FINISH_DATE);
        request.setDeleted(UPDATED_DELETED);

        restRequestMockMvc.perform(put("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk());

        // Validate the Request in the database
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeUpdate);
        Request testRequest = requests.get(requests.size() - 1);
        assertThat(testRequest.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRequest.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testRequest.getFinishDate()).isEqualTo(UPDATED_FINISH_DATE);
        assertThat(testRequest.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void deleteRequest() throws Exception {
        // Initialize the database
        requestRepository.saveAndFlush(request);

		int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Get the request
        restRequestMockMvc.perform(delete("/api/requests/{id}", request.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
