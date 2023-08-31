package com.innovasoftware.mockapi.service;

import com.innovasoftware.mockapi.domain.Resource;
import com.innovasoftware.mockapi.domain.ResourceSchema;
import com.innovasoftware.mockapi.repository.ResourceRepository;
import com.innovasoftware.mockapi.repository.ResourceSchemaRepository;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import com.innovasoftware.mockapi.service.mapper.ResourceMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Resource}.
 */
@Service
public class ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceService.class);

    private final ResourceRepository resourceRepository;

    private final ResourceMapper resourceMapper;

    @Autowired
    ResourceSchemaRepository resourceSchemaRepository;

    public ResourceService(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceDTO save(ResourceDTO resourceDTO) {
        log.debug("Request to save Resource : {}", resourceDTO);
        Resource resource = resourceMapper.toEntity(resourceDTO);
        resource = resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    /**
     * Update a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceDTO update(ResourceDTO resourceDTO) {
        log.debug("Request to update Resource : {}", resourceDTO);
        Resource resource = resourceMapper.toEntity(resourceDTO);
        resource = resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    /**
     * Partially update a resource.
     *
     * @param resourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResourceDTO> partialUpdate(ResourceDTO resourceDTO) {
        log.debug("Request to partially update Resource : {}", resourceDTO);

        return resourceRepository
            .findById(resourceDTO.getId())
            .map(existingResource -> {
                resourceMapper.partialUpdate(existingResource, resourceDTO);

                return existingResource;
            })
            .map(resourceRepository::save)
            .map(resourceMapper::toDto);
    }

    /**
     * Get all the resources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<ResourceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Resources");
        return resourceRepository.findAll(pageable).map(resourceMapper::toDto);
    }

    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ResourceDTO> findOne(String id) {
        log.debug("++++++Request to get Resource : {}", id);
        Resource resource = resourceRepository.findById(id).orElse(null);
        log.debug(resource.getResourceSchemas().toString());
        return resourceRepository.findById(id).map(resourceMapper::toDto);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Resource : {}", id);
        resourceRepository.deleteById(id);
    }

    public List<ResourceDTO> findByProjectId(String projectId) {
        log.debug("Request to get Resource by project id: {}", projectId);
        Resource resource = resourceRepository.findByProjectId(projectId).get(0);
        log.debug(resource.getResourceSchemas().toString());
        List<ResourceDTO> resourceDTO = resourceMapper.toDto(resourceRepository.findByProjectId(projectId));
        return resourceDTO;
    }
    public List<ResourceDTO> findAllWithEagerRelationshipsByProjectId(String projectId){
//        ObjectId id = new ObjectId(projectId);
        List<Resource> resources = resourceRepository.findByProjectId(projectId);
        resources.stream().forEach(resource -> {
            // Use Project repository to retrieve the project
            Set<ResourceSchema> resourceSchemas = resourceSchemaRepository.findAllByResourceId(resource.getId());
            resource.setResourceSchemas(resourceSchemas);
        });


        return resourceMapper.toDto(resources);
    }
}
