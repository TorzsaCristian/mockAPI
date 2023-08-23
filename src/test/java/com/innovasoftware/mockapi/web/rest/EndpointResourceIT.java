package com.innovasoftware.mockapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innovasoftware.mockapi.IntegrationTest;
import com.innovasoftware.mockapi.domain.Endpoint;
import com.innovasoftware.mockapi.repository.EndpointRepository;
import com.innovasoftware.mockapi.service.dto.EndpointDTO;
import com.innovasoftware.mockapi.service.mapper.EndpointMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link EndpointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EndpointResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/endpoints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private EndpointMapper endpointMapper;

    @Autowired
    private MockMvc restEndpointMockMvc;

    private Endpoint endpoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endpoint createEntity() {
        Endpoint endpoint = new Endpoint().url(DEFAULT_URL).method(DEFAULT_METHOD).enabled(DEFAULT_ENABLED).response(DEFAULT_RESPONSE);
        return endpoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endpoint createUpdatedEntity() {
        Endpoint endpoint = new Endpoint().url(UPDATED_URL).method(UPDATED_METHOD).enabled(UPDATED_ENABLED).response(UPDATED_RESPONSE);
        return endpoint;
    }

    @BeforeEach
    public void initTest() {
        endpointRepository.deleteAll();
        endpoint = createEntity();
    }

    @Test
    void createEndpoint() throws Exception {
        int databaseSizeBeforeCreate = endpointRepository.findAll().size();
        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);
        restEndpointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeCreate + 1);
        Endpoint testEndpoint = endpointList.get(endpointList.size() - 1);
        assertThat(testEndpoint.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testEndpoint.getMethod()).isEqualTo(DEFAULT_METHOD);
        assertThat(testEndpoint.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testEndpoint.getResponse()).isEqualTo(DEFAULT_RESPONSE);
    }

    @Test
    void createEndpointWithExistingId() throws Exception {
        // Create the Endpoint with an existing ID
        endpoint.setId("existing_id");
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        int databaseSizeBeforeCreate = endpointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEndpointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = endpointRepository.findAll().size();
        // set the field null
        endpoint.setUrl(null);

        // Create the Endpoint, which fails.
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        restEndpointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = endpointRepository.findAll().size();
        // set the field null
        endpoint.setMethod(null);

        // Create the Endpoint, which fails.
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        restEndpointMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEndpoints() throws Exception {
        // Initialize the database
        endpointRepository.save(endpoint);

        // Get all the endpointList
        restEndpointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endpoint.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE)));
    }

    @Test
    void getEndpoint() throws Exception {
        // Initialize the database
        endpointRepository.save(endpoint);

        // Get the endpoint
        restEndpointMockMvc
            .perform(get(ENTITY_API_URL_ID, endpoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(endpoint.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE));
    }

    @Test
    void getNonExistingEndpoint() throws Exception {
        // Get the endpoint
        restEndpointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingEndpoint() throws Exception {
        // Initialize the database
        endpointRepository.save(endpoint);

        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();

        // Update the endpoint
        Endpoint updatedEndpoint = endpointRepository.findById(endpoint.getId()).orElseThrow();
        updatedEndpoint.url(UPDATED_URL).method(UPDATED_METHOD).enabled(UPDATED_ENABLED).response(UPDATED_RESPONSE);
        EndpointDTO endpointDTO = endpointMapper.toDto(updatedEndpoint);

        restEndpointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, endpointDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isOk());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
        Endpoint testEndpoint = endpointList.get(endpointList.size() - 1);
        assertThat(testEndpoint.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testEndpoint.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testEndpoint.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testEndpoint.getResponse()).isEqualTo(UPDATED_RESPONSE);
    }

    @Test
    void putNonExistingEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();
        endpoint.setId(UUID.randomUUID().toString());

        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEndpointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, endpointDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();
        endpoint.setId(UUID.randomUUID().toString());

        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndpointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();
        endpoint.setId(UUID.randomUUID().toString());

        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndpointMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEndpointWithPatch() throws Exception {
        // Initialize the database
        endpointRepository.save(endpoint);

        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();

        // Update the endpoint using partial update
        Endpoint partialUpdatedEndpoint = new Endpoint();
        partialUpdatedEndpoint.setId(endpoint.getId());

        partialUpdatedEndpoint.method(UPDATED_METHOD).response(UPDATED_RESPONSE);

        restEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndpoint.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEndpoint))
            )
            .andExpect(status().isOk());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
        Endpoint testEndpoint = endpointList.get(endpointList.size() - 1);
        assertThat(testEndpoint.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testEndpoint.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testEndpoint.getEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testEndpoint.getResponse()).isEqualTo(UPDATED_RESPONSE);
    }

    @Test
    void fullUpdateEndpointWithPatch() throws Exception {
        // Initialize the database
        endpointRepository.save(endpoint);

        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();

        // Update the endpoint using partial update
        Endpoint partialUpdatedEndpoint = new Endpoint();
        partialUpdatedEndpoint.setId(endpoint.getId());

        partialUpdatedEndpoint.url(UPDATED_URL).method(UPDATED_METHOD).enabled(UPDATED_ENABLED).response(UPDATED_RESPONSE);

        restEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndpoint.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEndpoint))
            )
            .andExpect(status().isOk());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
        Endpoint testEndpoint = endpointList.get(endpointList.size() - 1);
        assertThat(testEndpoint.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testEndpoint.getMethod()).isEqualTo(UPDATED_METHOD);
        assertThat(testEndpoint.getEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testEndpoint.getResponse()).isEqualTo(UPDATED_RESPONSE);
    }

    @Test
    void patchNonExistingEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();
        endpoint.setId(UUID.randomUUID().toString());

        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, endpointDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();
        endpoint.setId(UUID.randomUUID().toString());

        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = endpointRepository.findAll().size();
        endpoint.setId(UUID.randomUUID().toString());

        // Create the Endpoint
        EndpointDTO endpointDTO = endpointMapper.toDto(endpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(endpointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endpoint in the database
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEndpoint() throws Exception {
        // Initialize the database
        endpointRepository.save(endpoint);

        int databaseSizeBeforeDelete = endpointRepository.findAll().size();

        // Delete the endpoint
        restEndpointMockMvc
            .perform(delete(ENTITY_API_URL_ID, endpoint.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Endpoint> endpointList = endpointRepository.findAll();
        assertThat(endpointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
