package com.innovasoftware.mockapi.service;

import com.innovasoftware.mockapi.domain.ResourceSchema;
import com.innovasoftware.mockapi.repository.ResourceSchemaRepository;
import com.innovasoftware.mockapi.service.dto.ResourceSchemaDTO;
import com.innovasoftware.mockapi.service.mapper.ResourceSchemaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ResourceSchema}.
 */
@Service
public class ResourceSchemaService {

    private final Logger log = LoggerFactory.getLogger(ResourceSchemaService.class);

    private final ResourceSchemaRepository resourceSchemaRepository;

    private final ResourceSchemaMapper resourceSchemaMapper;

    public ResourceSchemaService(ResourceSchemaRepository resourceSchemaRepository, ResourceSchemaMapper resourceSchemaMapper) {
        this.resourceSchemaRepository = resourceSchemaRepository;
        this.resourceSchemaMapper = resourceSchemaMapper;
    }

    /**
     * Save a resourceSchema.
     *
     * @param resourceSchemaDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceSchemaDTO save(ResourceSchemaDTO resourceSchemaDTO) {
        log.debug("Request to save ResourceSchema : {}", resourceSchemaDTO);
        ResourceSchema resourceSchema = resourceSchemaMapper.toEntity(resourceSchemaDTO);
        resourceSchema = resourceSchemaRepository.save(resourceSchema);
        return resourceSchemaMapper.toDto(resourceSchema);
    }

    /**
     * Update a resourceSchema.
     *
     * @param resourceSchemaDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceSchemaDTO update(ResourceSchemaDTO resourceSchemaDTO) {
        log.debug("Request to update ResourceSchema : {}", resourceSchemaDTO);
        ResourceSchema resourceSchema = resourceSchemaMapper.toEntity(resourceSchemaDTO);
        resourceSchema = resourceSchemaRepository.save(resourceSchema);
        return resourceSchemaMapper.toDto(resourceSchema);
    }

    /**
     * Partially update a resourceSchema.
     *
     * @param resourceSchemaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResourceSchemaDTO> partialUpdate(ResourceSchemaDTO resourceSchemaDTO) {
        log.debug("Request to partially update ResourceSchema : {}", resourceSchemaDTO);

        return resourceSchemaRepository
            .findById(resourceSchemaDTO.getId())
            .map(existingResourceSchema -> {
                resourceSchemaMapper.partialUpdate(existingResourceSchema, resourceSchemaDTO);

                return existingResourceSchema;
            })
            .map(resourceSchemaRepository::save)
            .map(resourceSchemaMapper::toDto);
    }

    /**
     * Get all the resourceSchemas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<ResourceSchemaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResourceSchemas");
        return resourceSchemaRepository.findAll(pageable).map(resourceSchemaMapper::toDto);
    }

    /**
     * Get one resourceSchema by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ResourceSchemaDTO> findOne(String id) {
        log.debug("Request to get ResourceSchema : {}", id);
        return resourceSchemaRepository.findById(id).map(resourceSchemaMapper::toDto);
    }

    /**
     * Delete the resourceSchema by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ResourceSchema : {}", id);
        resourceSchemaRepository.deleteById(id);
    }
}
