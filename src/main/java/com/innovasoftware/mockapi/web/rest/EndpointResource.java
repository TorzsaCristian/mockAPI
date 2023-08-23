package com.innovasoftware.mockapi.web.rest;

import com.innovasoftware.mockapi.repository.EndpointRepository;
import com.innovasoftware.mockapi.service.EndpointService;
import com.innovasoftware.mockapi.service.dto.EndpointDTO;
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
 * REST controller for managing {@link com.innovasoftware.mockapi.domain.Endpoint}.
 */
@RestController
@RequestMapping("/api")
public class EndpointResource {

    private final Logger log = LoggerFactory.getLogger(EndpointResource.class);

    private static final String ENTITY_NAME = "endpoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EndpointService endpointService;

    private final EndpointRepository endpointRepository;

    public EndpointResource(EndpointService endpointService, EndpointRepository endpointRepository) {
        this.endpointService = endpointService;
        this.endpointRepository = endpointRepository;
    }

    /**
     * {@code POST  /endpoints} : Create a new endpoint.
     *
     * @param endpointDTO the endpointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new endpointDTO, or with status {@code 400 (Bad Request)} if the endpoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/endpoints")
    public ResponseEntity<EndpointDTO> createEndpoint(@Valid @RequestBody EndpointDTO endpointDTO) throws URISyntaxException {
        log.debug("REST request to save Endpoint : {}", endpointDTO);
        if (endpointDTO.getId() != null) {
            throw new BadRequestAlertException("A new endpoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EndpointDTO result = endpointService.save(endpointDTO);
        return ResponseEntity
            .created(new URI("/api/endpoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /endpoints/:id} : Updates an existing endpoint.
     *
     * @param id the id of the endpointDTO to save.
     * @param endpointDTO the endpointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endpointDTO,
     * or with status {@code 400 (Bad Request)} if the endpointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the endpointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/endpoints/{id}")
    public ResponseEntity<EndpointDTO> updateEndpoint(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody EndpointDTO endpointDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Endpoint : {}, {}", id, endpointDTO);
        if (endpointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, endpointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!endpointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EndpointDTO result = endpointService.update(endpointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endpointDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /endpoints/:id} : Partial updates given fields of an existing endpoint, field will ignore if it is null
     *
     * @param id the id of the endpointDTO to save.
     * @param endpointDTO the endpointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated endpointDTO,
     * or with status {@code 400 (Bad Request)} if the endpointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the endpointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the endpointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/endpoints/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EndpointDTO> partialUpdateEndpoint(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody EndpointDTO endpointDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Endpoint partially : {}, {}", id, endpointDTO);
        if (endpointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, endpointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!endpointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EndpointDTO> result = endpointService.partialUpdate(endpointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, endpointDTO.getId())
        );
    }

    /**
     * {@code GET  /endpoints} : get all the endpoints.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of endpoints in body.
     */
    @GetMapping("/endpoints")
    public ResponseEntity<List<EndpointDTO>> getAllEndpoints(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Endpoints");
        Page<EndpointDTO> page = endpointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /endpoints/:id} : get the "id" endpoint.
     *
     * @param id the id of the endpointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the endpointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/endpoints/{id}")
    public ResponseEntity<EndpointDTO> getEndpoint(@PathVariable String id) {
        log.debug("REST request to get Endpoint : {}", id);
        Optional<EndpointDTO> endpointDTO = endpointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(endpointDTO);
    }

    /**
     * {@code DELETE  /endpoints/:id} : delete the "id" endpoint.
     *
     * @param id the id of the endpointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/endpoints/{id}")
    public ResponseEntity<Void> deleteEndpoint(@PathVariable String id) {
        log.debug("REST request to delete Endpoint : {}", id);
        endpointService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
