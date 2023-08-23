package com.innovasoftware.mockapi.web.rest;

import com.innovasoftware.mockapi.repository.MockRepository;
import com.innovasoftware.mockapi.service.MockService;
import com.innovasoftware.mockapi.service.dto.MockDTO;
import com.innovasoftware.mockapi.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.innovasoftware.mockapi.domain.Mock}.
 */
@RestController
@RequestMapping("/api")
public class MockResource {

    private final Logger log = LoggerFactory.getLogger(MockResource.class);

    private static final String ENTITY_NAME = "mock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MockService mockService;

    private final MockRepository mockRepository;

    public MockResource(MockService mockService, MockRepository mockRepository) {
        this.mockService = mockService;
        this.mockRepository = mockRepository;
    }

    /**
     * {@code POST  /mocks} : Create a new mock.
     *
     * @param mockDTO the mockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mockDTO, or with status {@code 400 (Bad Request)} if the mock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mocks")
    public ResponseEntity<MockDTO> createMock(@RequestBody MockDTO mockDTO) throws URISyntaxException {
        log.debug("REST request to save Mock : {}", mockDTO);
        if (mockDTO.getId() != null) {
            throw new BadRequestAlertException("A new mock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MockDTO result = mockService.save(mockDTO);
        return ResponseEntity
            .created(new URI("/api/mocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /mocks/:id} : Updates an existing mock.
     *
     * @param id the id of the mockDTO to save.
     * @param mockDTO the mockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mockDTO,
     * or with status {@code 400 (Bad Request)} if the mockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mocks/{id}")
    public ResponseEntity<MockDTO> updateMock(@PathVariable(value = "id", required = false) final String id, @RequestBody MockDTO mockDTO)
        throws URISyntaxException {
        log.debug("REST request to update Mock : {}, {}", id, mockDTO);
        if (mockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MockDTO result = mockService.update(mockDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mockDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /mocks/:id} : Partial updates given fields of an existing mock, field will ignore if it is null
     *
     * @param id the id of the mockDTO to save.
     * @param mockDTO the mockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mockDTO,
     * or with status {@code 400 (Bad Request)} if the mockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mocks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MockDTO> partialUpdateMock(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody MockDTO mockDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mock partially : {}, {}", id, mockDTO);
        if (mockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MockDTO> result = mockService.partialUpdate(mockDTO);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mockDTO.getId()));
    }

    /**
     * {@code GET  /mocks} : get all the mocks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mocks in body.
     */
    @GetMapping("/mocks")
    public ResponseEntity<List<MockDTO>> getAllMocks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Mocks");
        Page<MockDTO> page = mockService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mocks/:id} : get the "id" mock.
     *
     * @param id the id of the mockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mocks/{id}")
    public ResponseEntity<MockDTO> getMock(@PathVariable String id) {
        log.debug("REST request to get Mock : {}", id);
        Optional<MockDTO> mockDTO = mockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mockDTO);
    }

    /**
     * {@code DELETE  /mocks/:id} : delete the "id" mock.
     *
     * @param id the id of the mockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mocks/{id}")
    public ResponseEntity<Void> deleteMock(@PathVariable String id) {
        log.debug("REST request to delete Mock : {}", id);
        mockService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
