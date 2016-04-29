package com.hielfsoft.volunteercrowd.web.rest;

import com.hielfsoft.volunteercrowd.VolunteercrowdApp;
import com.hielfsoft.volunteercrowd.domain.Request;
import com.hielfsoft.volunteercrowd.domain.RequestStatus;
import com.hielfsoft.volunteercrowd.domain.RequestStatusConstants;
import com.hielfsoft.volunteercrowd.repository.RequestRepository;
import com.hielfsoft.volunteercrowd.repository.search.RequestSearchRepository;
import com.hielfsoft.volunteercrowd.service.AppUserService;
import com.hielfsoft.volunteercrowd.service.NeedService;
import com.hielfsoft.volunteercrowd.service.RequestService;
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
 * Test class for the RequestResource REST controller.
 *
 * @see RequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VolunteercrowdApp.class)
@WebAppConfiguration
@IntegrationTest
public class RequestResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_FINISH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FINISH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FINISH_DATE_STR = dateTimeFormatter.format(DEFAULT_FINISH_DATE);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final ZonedDateTime DEFAULT_MODIFICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_DATE);

    private static final long APPLICANT_ID = 12;
    private static final long NEED_ID = 42;

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private RequestService requestService;

    @Inject
    private RequestSearchRepository requestSearchRepository;

    @Inject
    private AppUserService appUserService;

    @Inject
    private NeedService needService;

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
        requestSearchRepository.deleteAll();
        request = new Request();
        request.setCreationDate(DEFAULT_CREATION_DATE);
        request.setDescription(DEFAULT_DESCRIPTION);
        request.setCode(DEFAULT_CODE);
        request.setFinishDate(DEFAULT_FINISH_DATE);
        request.setDeleted(DEFAULT_DELETED);
        request.setPaid(DEFAULT_PAID);
        request.setModificationDate(DEFAULT_MODIFICATION_DATE);

        request.setApplicant(appUserService.findOne(APPLICANT_ID));
        request.setNeed(needService.findOne(NEED_ID));
        RequestStatus rs = new RequestStatus();
        rs.setName(RequestStatusConstants.PENDING);
        request.setRequestStatus(rs);
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
        assertThat(testRequest.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testRequest.isPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testRequest.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);

        // Validate the Request in ElasticSearch
        Request requestEs = requestSearchRepository.findOne(testRequest.getId());
        assertThat(requestEs).isEqualToComparingFieldByField(testRequest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setCreationDate(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setDescription(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setCode(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setFinishDate(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setDeleted(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setPaid(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModificationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = requestRepository.findAll().size();
        // set the field null
        request.setModificationDate(null);

        // Create the Request, which fails.

        restRequestMockMvc.perform(post("/api/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isBadRequest());

        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeTest);
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
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE_STR)))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
                .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)));
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
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.finishDate").value(DEFAULT_FINISH_DATE_STR))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE_STR));
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
        requestService.save(request);

        int databaseSizeBeforeUpdate = requestRepository.findAll().size();

        // Update the request
        request.setId(request.getId());
        request.setCreationDate(UPDATED_CREATION_DATE);
        request.setDescription(UPDATED_DESCRIPTION);
        request.setCode(UPDATED_CODE);
        request.setFinishDate(UPDATED_FINISH_DATE);
        request.setDeleted(UPDATED_DELETED);
        request.setPaid(UPDATED_PAID);
        request.setModificationDate(UPDATED_MODIFICATION_DATE);

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
        assertThat(testRequest.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testRequest.isPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testRequest.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);

        // Validate the Request in ElasticSearch
        Request requestEs = requestSearchRepository.findOne(testRequest.getId());
        assertThat(requestEs).isEqualToComparingFieldByField(testRequest);
    }

    @Test
    @Transactional
    public void deleteRequest() throws Exception {
        // Initialize the database
        requestService.save(request);

        int databaseSizeBeforeDelete = requestRepository.findAll().size();

        // Get the request
        restRequestMockMvc.perform(delete("/api/requests/{id}", request.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean requestExistsInEs = requestSearchRepository.exists(request.getId());
        assertThat(requestExistsInEs).isFalse();

        // Validate the database is empty
        List<Request> requests = requestRepository.findAll();
        assertThat(requests).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRequest() throws Exception {
        // Initialize the database
        requestService.save(request);

        // Search the request
        restRequestMockMvc.perform(get("/api/_search/requests?query=id:" + request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].finishDate").value(hasItem(DEFAULT_FINISH_DATE_STR)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE_STR)));
    }
}
