package com.innovasoftware.mockapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innovasoftware.mockapi.IntegrationTest;
import com.innovasoftware.mockapi.domain.Mock;
import com.innovasoftware.mockapi.repository.MockRepository;
import com.innovasoftware.mockapi.service.dto.MockDTO;
import com.innovasoftware.mockapi.service.mapper.MockMapper;
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
 * Integration tests for the {@link MockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MockResourceIT {

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String ENTITY_API_URL = "/api/mocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MockRepository mockRepository;

    @Autowired
    private MockMapper mockMapper;

    @Autowired
    private MockMvc restMockMockMvc;

    private Mock mock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mock createEntity() {
        Mock mock = new Mock().prefix(DEFAULT_PREFIX).version(DEFAULT_VERSION);
        return mock;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mock createUpdatedEntity() {
        Mock mock = new Mock().prefix(UPDATED_PREFIX).version(UPDATED_VERSION);
        return mock;
    }

    @BeforeEach
    public void initTest() {
        mockRepository.deleteAll();
        mock = createEntity();
    }

    @Test
    void createMock() throws Exception {
        int databaseSizeBeforeCreate = mockRepository.findAll().size();
        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);
        restMockMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeCreate + 1);
        Mock testMock = mockList.get(mockList.size() - 1);
        assertThat(testMock.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testMock.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    void createMockWithExistingId() throws Exception {
        // Create the Mock with an existing ID
        mock.setId("existing_id");
        MockDTO mockDTO = mockMapper.toDto(mock);

        int databaseSizeBeforeCreate = mockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMockMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMocks() throws Exception {
        // Initialize the database
        mockRepository.save(mock);

        // Get all the mockList
        restMockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mock.getId())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    void getMock() throws Exception {
        // Initialize the database
        mockRepository.save(mock);

        // Get the mock
        restMockMockMvc
            .perform(get(ENTITY_API_URL_ID, mock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mock.getId()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    void getNonExistingMock() throws Exception {
        // Get the mock
        restMockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMock() throws Exception {
        // Initialize the database
        mockRepository.save(mock);

        int databaseSizeBeforeUpdate = mockRepository.findAll().size();

        // Update the mock
        Mock updatedMock = mockRepository.findById(mock.getId()).orElseThrow();
        updatedMock.prefix(UPDATED_PREFIX).version(UPDATED_VERSION);
        MockDTO mockDTO = mockMapper.toDto(updatedMock);

        restMockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mockDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
        Mock testMock = mockList.get(mockList.size() - 1);
        assertThat(testMock.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testMock.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    void putNonExistingMock() throws Exception {
        int databaseSizeBeforeUpdate = mockRepository.findAll().size();
        mock.setId(UUID.randomUUID().toString());

        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mockDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMock() throws Exception {
        int databaseSizeBeforeUpdate = mockRepository.findAll().size();
        mock.setId(UUID.randomUUID().toString());

        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMock() throws Exception {
        int databaseSizeBeforeUpdate = mockRepository.findAll().size();
        mock.setId(UUID.randomUUID().toString());

        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMockMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMockWithPatch() throws Exception {
        // Initialize the database
        mockRepository.save(mock);

        int databaseSizeBeforeUpdate = mockRepository.findAll().size();

        // Update the mock using partial update
        Mock partialUpdatedMock = new Mock();
        partialUpdatedMock.setId(mock.getId());

        partialUpdatedMock.prefix(UPDATED_PREFIX);

        restMockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMock.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMock))
            )
            .andExpect(status().isOk());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
        Mock testMock = mockList.get(mockList.size() - 1);
        assertThat(testMock.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testMock.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    void fullUpdateMockWithPatch() throws Exception {
        // Initialize the database
        mockRepository.save(mock);

        int databaseSizeBeforeUpdate = mockRepository.findAll().size();

        // Update the mock using partial update
        Mock partialUpdatedMock = new Mock();
        partialUpdatedMock.setId(mock.getId());

        partialUpdatedMock.prefix(UPDATED_PREFIX).version(UPDATED_VERSION);

        restMockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMock.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMock))
            )
            .andExpect(status().isOk());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
        Mock testMock = mockList.get(mockList.size() - 1);
        assertThat(testMock.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testMock.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    void patchNonExistingMock() throws Exception {
        int databaseSizeBeforeUpdate = mockRepository.findAll().size();
        mock.setId(UUID.randomUUID().toString());

        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mockDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMock() throws Exception {
        int databaseSizeBeforeUpdate = mockRepository.findAll().size();
        mock.setId(UUID.randomUUID().toString());

        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMock() throws Exception {
        int databaseSizeBeforeUpdate = mockRepository.findAll().size();
        mock.setId(UUID.randomUUID().toString());

        // Create the Mock
        MockDTO mockDTO = mockMapper.toDto(mock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMockMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mockDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mock in the database
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMock() throws Exception {
        // Initialize the database
        mockRepository.save(mock);

        int databaseSizeBeforeDelete = mockRepository.findAll().size();

        // Delete the mock
        restMockMockMvc
            .perform(delete(ENTITY_API_URL_ID, mock.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mock> mockList = mockRepository.findAll();
        assertThat(mockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
