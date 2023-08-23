package com.innovasoftware.mockapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innovasoftware.mockapi.IntegrationTest;
import com.innovasoftware.mockapi.domain.ResourceSchema;
import com.innovasoftware.mockapi.repository.ResourceSchemaRepository;
import com.innovasoftware.mockapi.service.dto.ResourceSchemaDTO;
import com.innovasoftware.mockapi.service.mapper.ResourceSchemaMapper;
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
 * Integration tests for the {@link ResourceSchemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceSchemaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FAKER_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_FAKER_METHOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resource-schemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ResourceSchemaRepository resourceSchemaRepository;

    @Autowired
    private ResourceSchemaMapper resourceSchemaMapper;

    @Autowired
    private MockMvc restResourceSchemaMockMvc;

    private ResourceSchema resourceSchema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceSchema createEntity() {
        ResourceSchema resourceSchema = new ResourceSchema().name(DEFAULT_NAME).type(DEFAULT_TYPE).fakerMethod(DEFAULT_FAKER_METHOD);
        return resourceSchema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceSchema createUpdatedEntity() {
        ResourceSchema resourceSchema = new ResourceSchema().name(UPDATED_NAME).type(UPDATED_TYPE).fakerMethod(UPDATED_FAKER_METHOD);
        return resourceSchema;
    }

    @BeforeEach
    public void initTest() {
        resourceSchemaRepository.deleteAll();
        resourceSchema = createEntity();
    }

    @Test
    void createResourceSchema() throws Exception {
        int databaseSizeBeforeCreate = resourceSchemaRepository.findAll().size();
        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);
        restResourceSchemaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceSchema testResourceSchema = resourceSchemaList.get(resourceSchemaList.size() - 1);
        assertThat(testResourceSchema.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResourceSchema.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testResourceSchema.getFakerMethod()).isEqualTo(DEFAULT_FAKER_METHOD);
    }

    @Test
    void createResourceSchemaWithExistingId() throws Exception {
        // Create the ResourceSchema with an existing ID
        resourceSchema.setId("existing_id");
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        int databaseSizeBeforeCreate = resourceSchemaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceSchemaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceSchemaRepository.findAll().size();
        // set the field null
        resourceSchema.setName(null);

        // Create the ResourceSchema, which fails.
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        restResourceSchemaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceSchemaRepository.findAll().size();
        // set the field null
        resourceSchema.setType(null);

        // Create the ResourceSchema, which fails.
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        restResourceSchemaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllResourceSchemas() throws Exception {
        // Initialize the database
        resourceSchemaRepository.save(resourceSchema);

        // Get all the resourceSchemaList
        restResourceSchemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceSchema.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].fakerMethod").value(hasItem(DEFAULT_FAKER_METHOD)));
    }

    @Test
    void getResourceSchema() throws Exception {
        // Initialize the database
        resourceSchemaRepository.save(resourceSchema);

        // Get the resourceSchema
        restResourceSchemaMockMvc
            .perform(get(ENTITY_API_URL_ID, resourceSchema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceSchema.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.fakerMethod").value(DEFAULT_FAKER_METHOD));
    }

    @Test
    void getNonExistingResourceSchema() throws Exception {
        // Get the resourceSchema
        restResourceSchemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingResourceSchema() throws Exception {
        // Initialize the database
        resourceSchemaRepository.save(resourceSchema);

        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();

        // Update the resourceSchema
        ResourceSchema updatedResourceSchema = resourceSchemaRepository.findById(resourceSchema.getId()).orElseThrow();
        updatedResourceSchema.name(UPDATED_NAME).type(UPDATED_TYPE).fakerMethod(UPDATED_FAKER_METHOD);
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(updatedResourceSchema);

        restResourceSchemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceSchemaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
        ResourceSchema testResourceSchema = resourceSchemaList.get(resourceSchemaList.size() - 1);
        assertThat(testResourceSchema.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResourceSchema.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testResourceSchema.getFakerMethod()).isEqualTo(UPDATED_FAKER_METHOD);
    }

    @Test
    void putNonExistingResourceSchema() throws Exception {
        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();
        resourceSchema.setId(UUID.randomUUID().toString());

        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceSchemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceSchemaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResourceSchema() throws Exception {
        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();
        resourceSchema.setId(UUID.randomUUID().toString());

        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceSchemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResourceSchema() throws Exception {
        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();
        resourceSchema.setId(UUID.randomUUID().toString());

        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceSchemaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResourceSchemaWithPatch() throws Exception {
        // Initialize the database
        resourceSchemaRepository.save(resourceSchema);

        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();

        // Update the resourceSchema using partial update
        ResourceSchema partialUpdatedResourceSchema = new ResourceSchema();
        partialUpdatedResourceSchema.setId(resourceSchema.getId());

        partialUpdatedResourceSchema.name(UPDATED_NAME).type(UPDATED_TYPE);

        restResourceSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceSchema.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceSchema))
            )
            .andExpect(status().isOk());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
        ResourceSchema testResourceSchema = resourceSchemaList.get(resourceSchemaList.size() - 1);
        assertThat(testResourceSchema.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResourceSchema.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testResourceSchema.getFakerMethod()).isEqualTo(DEFAULT_FAKER_METHOD);
    }

    @Test
    void fullUpdateResourceSchemaWithPatch() throws Exception {
        // Initialize the database
        resourceSchemaRepository.save(resourceSchema);

        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();

        // Update the resourceSchema using partial update
        ResourceSchema partialUpdatedResourceSchema = new ResourceSchema();
        partialUpdatedResourceSchema.setId(resourceSchema.getId());

        partialUpdatedResourceSchema.name(UPDATED_NAME).type(UPDATED_TYPE).fakerMethod(UPDATED_FAKER_METHOD);

        restResourceSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResourceSchema.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResourceSchema))
            )
            .andExpect(status().isOk());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
        ResourceSchema testResourceSchema = resourceSchemaList.get(resourceSchemaList.size() - 1);
        assertThat(testResourceSchema.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResourceSchema.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testResourceSchema.getFakerMethod()).isEqualTo(UPDATED_FAKER_METHOD);
    }

    @Test
    void patchNonExistingResourceSchema() throws Exception {
        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();
        resourceSchema.setId(UUID.randomUUID().toString());

        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceSchemaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResourceSchema() throws Exception {
        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();
        resourceSchema.setId(UUID.randomUUID().toString());

        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResourceSchema() throws Exception {
        int databaseSizeBeforeUpdate = resourceSchemaRepository.findAll().size();
        resourceSchema.setId(UUID.randomUUID().toString());

        // Create the ResourceSchema
        ResourceSchemaDTO resourceSchemaDTO = resourceSchemaMapper.toDto(resourceSchema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceSchemaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResourceSchema in the database
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResourceSchema() throws Exception {
        // Initialize the database
        resourceSchemaRepository.save(resourceSchema);

        int databaseSizeBeforeDelete = resourceSchemaRepository.findAll().size();

        // Delete the resourceSchema
        restResourceSchemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, resourceSchema.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourceSchema> resourceSchemaList = resourceSchemaRepository.findAll();
        assertThat(resourceSchemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
