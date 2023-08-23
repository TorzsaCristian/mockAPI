package com.innovasoftware.mockapi.service;

import com.innovasoftware.mockapi.domain.Mock;
import com.innovasoftware.mockapi.repository.MockRepository;
import com.innovasoftware.mockapi.service.dto.MockDTO;
import com.innovasoftware.mockapi.service.mapper.MockMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Mock}.
 */
@Service
public class MockService {

    private final Logger log = LoggerFactory.getLogger(MockService.class);

    private final MockRepository mockRepository;

    private final MockMapper mockMapper;

    public MockService(MockRepository mockRepository, MockMapper mockMapper) {
        this.mockRepository = mockRepository;
        this.mockMapper = mockMapper;
    }

    /**
     * Save a mock.
     *
     * @param mockDTO the entity to save.
     * @return the persisted entity.
     */
    public MockDTO save(MockDTO mockDTO) {
        log.debug("Request to save Mock : {}", mockDTO);
        Mock mock = mockMapper.toEntity(mockDTO);
        mock = mockRepository.save(mock);
        return mockMapper.toDto(mock);
    }

    /**
     * Update a mock.
     *
     * @param mockDTO the entity to save.
     * @return the persisted entity.
     */
    public MockDTO update(MockDTO mockDTO) {
        log.debug("Request to update Mock : {}", mockDTO);
        Mock mock = mockMapper.toEntity(mockDTO);
        mock = mockRepository.save(mock);
        return mockMapper.toDto(mock);
    }

    /**
     * Partially update a mock.
     *
     * @param mockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MockDTO> partialUpdate(MockDTO mockDTO) {
        log.debug("Request to partially update Mock : {}", mockDTO);

        return mockRepository
            .findById(mockDTO.getId())
            .map(existingMock -> {
                mockMapper.partialUpdate(existingMock, mockDTO);

                return existingMock;
            })
            .map(mockRepository::save)
            .map(mockMapper::toDto);
    }

    /**
     * Get all the mocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<MockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mocks");
        return mockRepository.findAll(pageable).map(mockMapper::toDto);
    }

    /**
     * Get one mock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MockDTO> findOne(String id) {
        log.debug("Request to get Mock : {}", id);
        return mockRepository.findById(id).map(mockMapper::toDto);
    }

    /**
     * Delete the mock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Mock : {}", id);
        mockRepository.deleteById(id);
    }
}
