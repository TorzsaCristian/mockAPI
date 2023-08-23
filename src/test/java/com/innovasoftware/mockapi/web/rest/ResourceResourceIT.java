package com.innovasoftware.mockapi.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.innovasoftware.mockapi.IntegrationTest;
import com.innovasoftware.mockapi.domain.Resource;
import com.innovasoftware.mockapi.repository.ResourceRepository;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import com.innovasoftware.mockapi.service.mapper.ResourceMapper;
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
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENERATOR = "AAAAAAAAAA";
    private static final String UPDATED_GENERATOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_COUNT = 1;
    private static final Integer UPDATED_COUNT = 2;

    private static final String ENTITY_API_URL = "/api/resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private MockMvc restResourceMockMvc;

    private Resource resource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity() {
        Resource resource = new Resource().name(DEFAULT_NAME).generator(DEFAULT_GENERATOR).count(DEFAULT_COUNT);
        return resource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity() {
        Resource resource = new Resource().name(UPDATED_NAME).generator(UPDATED_GENERATOR).count(UPDATED_COUNT);
        return resource;
    }

    @BeforeEach
    public void initTest() {
        resourceRepository.deleteAll();
        resource = createEntity();
    }

    @Test
    void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();
        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);
        restResourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResource.getGenerator()).isEqualTo(DEFAULT_GENERATOR);
        assertThat(testResource.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    void createResourceWithExistingId() throws Exception {
        // Create the Resource with an existing ID
        resource.setId("existing_id");
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourceRepository.findAll().size();
        // set the field null
        resource.setName(null);

        // Create the Resource, which fails.
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        restResourceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.save(resource);

        // Get all the resourceList
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].generator").value(hasItem(DEFAULT_GENERATOR)))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)));
    }

    @Test
    void getResource() throws Exception {
        // Initialize the database
        resourceRepository.save(resource);

        // Get the resource
        restResourceMockMvc
            .perform(get(ENTITY_API_URL_ID, resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.generator").value(DEFAULT_GENERATOR))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT));
    }

    @Test
    void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingResource() throws Exception {
        // Initialize the database
        resourceRepository.save(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).orElseThrow();
        updatedResource.name(UPDATED_NAME).generator(UPDATED_GENERATOR).count(UPDATED_COUNT);
        ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getGenerator()).isEqualTo(UPDATED_GENERATOR);
        assertThat(testResource.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    void putNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resourceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        resourceRepository.save(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResource.getGenerator()).isEqualTo(DEFAULT_GENERATOR);
        assertThat(testResource.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    void fullUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        resourceRepository.save(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource.name(UPDATED_NAME).generator(UPDATED_GENERATOR).count(UPDATED_COUNT);

        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResource.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResource.getGenerator()).isEqualTo(UPDATED_GENERATOR);
        assertThat(testResource.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    void patchNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resourceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(UUID.randomUUID().toString());

        // Create the Resource
        ResourceDTO resourceDTO = resourceMapper.toDto(resource);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resourceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.save(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Delete the resource
        restResourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, resource.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
