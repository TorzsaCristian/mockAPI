package com.innovasoftware.mockapi.service;

import com.innovasoftware.mockapi.domain.Endpoint;
import com.innovasoftware.mockapi.repository.EndpointRepository;
import com.innovasoftware.mockapi.service.dto.EndpointDTO;
import com.innovasoftware.mockapi.service.mapper.EndpointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Endpoint}.
 */
@Service
public class EndpointService {

    private final Logger log = LoggerFactory.getLogger(EndpointService.class);

    private final EndpointRepository endpointRepository;

    private final EndpointMapper endpointMapper;

    public EndpointService(EndpointRepository endpointRepository, EndpointMapper endpointMapper) {
        this.endpointRepository = endpointRepository;
        this.endpointMapper = endpointMapper;
    }

    /**
     * Save a endpoint.
     *
     * @param endpointDTO the entity to save.
     * @return the persisted entity.
     */
    public EndpointDTO save(EndpointDTO endpointDTO) {
        log.debug("Request to save Endpoint : {}", endpointDTO);
        Endpoint endpoint = endpointMapper.toEntity(endpointDTO);
        endpoint = endpointRepository.save(endpoint);
        return endpointMapper.toDto(endpoint);
    }

    /**
     * Update a endpoint.
     *
     * @param endpointDTO the entity to save.
     * @return the persisted entity.
     */
    public EndpointDTO update(EndpointDTO endpointDTO) {
        log.debug("Request to update Endpoint : {}", endpointDTO);
        Endpoint endpoint = endpointMapper.toEntity(endpointDTO);
        endpoint = endpointRepository.save(endpoint);
        return endpointMapper.toDto(endpoint);
    }

    /**
     * Partially update a endpoint.
     *
     * @param endpointDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EndpointDTO> partialUpdate(EndpointDTO endpointDTO) {
        log.debug("Request to partially update Endpoint : {}", endpointDTO);

        return endpointRepository
            .findById(endpointDTO.getId())
            .map(existingEndpoint -> {
                endpointMapper.partialUpdate(existingEndpoint, endpointDTO);

                return existingEndpoint;
            })
            .map(endpointRepository::save)
            .map(endpointMapper::toDto);
    }

    /**
     * Get all the endpoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<EndpointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Endpoints");
        return endpointRepository.findAll(pageable).map(endpointMapper::toDto);
    }

    /**
     * Get one endpoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<EndpointDTO> findOne(String id) {
        log.debug("Request to get Endpoint : {}", id);
        return endpointRepository.findById(id).map(endpointMapper::toDto);
    }

    /**
     * Delete the endpoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Endpoint : {}", id);
        endpointRepository.deleteById(id);
    }
}
