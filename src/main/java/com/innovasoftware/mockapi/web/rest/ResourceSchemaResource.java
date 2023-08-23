package com.innovasoftware.mockapi.web.rest;

import com.innovasoftware.mockapi.repository.ResourceSchemaRepository;
import com.innovasoftware.mockapi.service.ResourceSchemaService;
import com.innovasoftware.mockapi.service.dto.ResourceSchemaDTO;
import com.innovasoftware.mockapi.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.innovasoftware.mockapi.domain.ResourceSchema}.
 */
@RestController
@RequestMapping("/api")
public class ResourceSchemaResource {

    private final Logger log = LoggerFactory.getLogger(ResourceSchemaResource.class);

    private static final String ENTITY_NAME = "resourceSchema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceSchemaService resourceSchemaService;

    private final ResourceSchemaRepository resourceSchemaRepository;

    public ResourceSchemaResource(ResourceSchemaService resourceSchemaService, ResourceSchemaRepository resourceSchemaRepository) {
        this.resourceSchemaService = resourceSchemaService;
        this.resourceSchemaRepository = resourceSchemaRepository;
    }

    /**
     * {@code POST  /resource-schemas} : Create a new resourceSchema.
     *
     * @param resourceSchemaDTO the resourceSchemaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceSchemaDTO, or with status {@code 400 (Bad Request)} if the resourceSchema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-schemas")
    public ResponseEntity<ResourceSchemaDTO> createResourceSchema(@Valid @RequestBody ResourceSchemaDTO resourceSchemaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResourceSchema : {}", resourceSchemaDTO);
        if (resourceSchemaDTO.getId() != null) {
            throw new BadRequestAlertException("A new resourceSchema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceSchemaDTO result = resourceSchemaService.save(resourceSchemaDTO);
        return ResponseEntity
            .created(new URI("/api/resource-schemas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-schemas/:id} : Updates an existing resourceSchema.
     *
     * @param id the id of the resourceSchemaDTO to save.
     * @param resourceSchemaDTO the resourceSchemaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceSchemaDTO,
     * or with status {@code 400 (Bad Request)} if the resourceSchemaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceSchemaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-schemas/{id}")
    public ResponseEntity<ResourceSchemaDTO> updateResourceSchema(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ResourceSchemaDTO resourceSchemaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResourceSchema : {}, {}", id, resourceSchemaDTO);
        if (resourceSchemaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceSchemaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceSchemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResourceSchemaDTO result = resourceSchemaService.update(resourceSchemaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceSchemaDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /resource-schemas/:id} : Partial updates given fields of an existing resourceSchema, field will ignore if it is null
     *
     * @param id the id of the resourceSchemaDTO to save.
     * @param resourceSchemaDTO the resourceSchemaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceSchemaDTO,
     * or with status {@code 400 (Bad Request)} if the resourceSchemaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resourceSchemaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resourceSchemaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resource-schemas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResourceSchemaDTO> partialUpdateResourceSchema(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ResourceSchemaDTO resourceSchemaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResourceSchema partially : {}, {}", id, resourceSchemaDTO);
        if (resourceSchemaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resourceSchemaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resourceSchemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResourceSchemaDTO> result = resourceSchemaService.partialUpdate(resourceSchemaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resourceSchemaDTO.getId())
        );
    }

    /**
     * {@code GET  /resource-schemas} : get all the resourceSchemas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceSchemas in body.
     */
    @GetMapping("/resource-schemas")
    public ResponseEntity<List<ResourceSchemaDTO>> getAllResourceSchemas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ResourceSchemas");
        Page<ResourceSchemaDTO> page = resourceSchemaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resource-schemas/:id} : get the "id" resourceSchema.
     *
     * @param id the id of the resourceSchemaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceSchemaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-schemas/{id}")
    public ResponseEntity<ResourceSchemaDTO> getResourceSchema(@PathVariable String id) {
        log.debug("REST request to get ResourceSchema : {}", id);
        Optional<ResourceSchemaDTO> resourceSchemaDTO = resourceSchemaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resourceSchemaDTO);
    }

    /**
     * {@code DELETE  /resource-schemas/:id} : delete the "id" resourceSchema.
     *
     * @param id the id of the resourceSchemaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-schemas/{id}")
    public ResponseEntity<Void> deleteResourceSchema(@PathVariable String id) {
        log.debug("REST request to delete ResourceSchema : {}", id);
        resourceSchemaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
