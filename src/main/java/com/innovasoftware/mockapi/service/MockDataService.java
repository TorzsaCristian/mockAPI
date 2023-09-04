package com.innovasoftware.mockapi.service;

import com.innovasoftware.mockapi.domain.MockData;
import com.innovasoftware.mockapi.repository.MockDataRepository;
import com.innovasoftware.mockapi.service.dto.MockDataDTO;
import com.innovasoftware.mockapi.service.mapper.MockDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MockData}.
 */
@Service
public class MockDataService {

    private final Logger log = LoggerFactory.getLogger(MockDataService.class);

    private final MockDataRepository mockDataRepository;

    private final MockDataMapper mockDataMapper;

    public MockDataService(MockDataRepository mockDataRepository, MockDataMapper mockDataMapper) {
        this.mockDataRepository = mockDataRepository;
        this.mockDataMapper = mockDataMapper;
    }

    /**
     * Save a mock.
     *
     * @param mockDataDTO the entity to save.
     * @return the persisted entity.
     */
    public MockDataDTO save(MockDataDTO mockDataDTO) {
        log.debug("Request to save Mock : {}", mockDataDTO);
        MockData mockData = mockDataMapper.toEntity(mockDataDTO);
        mockData = mockDataRepository.save(mockData);
        return mockDataMapper.toDto(mockData);
    }

    /**
     * Update a mock.
     *
     * @param mockDataDTO the entity to save.
     * @return the persisted entity.
     */
    public MockDataDTO update(MockDataDTO mockDataDTO) {
        log.debug("Request to update Mock : {}", mockDataDTO);
        MockData mockData = mockDataMapper.toEntity(mockDataDTO);
        mockData = mockDataRepository.save(mockData);
        return mockDataMapper.toDto(mockData);
    }

    /**
     * Partially update a mockData.
     *
     * @param mockDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MockDataDTO> partialUpdate(MockDataDTO mockDataDTO) {
        log.debug("Request to partially update Mock : {}", mockDataDTO);

        return mockDataRepository
            .findById(mockDataDTO.getId())
            .map(existingMock -> {
                mockDataMapper.partialUpdate(existingMock, mockDataDTO);

                return existingMock;
            })
            .map(mockDataRepository::save)
            .map(mockDataMapper::toDto);
    }

    /**
     * Get all the mockDatas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<MockDataDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mocks");
        return mockDataRepository.findAll(pageable).map(mockDataMapper::toDto);
    }

    /**
     * Get one mockData by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MockDataDTO> findOne(String id) {
        log.debug("Request to get mockDataDTO : {}", id);
        return mockDataRepository.findById(id).map(mockDataMapper::toDto);
    }

    /**
     * Delete the mockData by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete MockData : {}", id);
        mockDataRepository.deleteById(id);
    }

    public Optional<MockDataDTO> findByResourceId(String resourceId) {
        return mockDataRepository.findByResourceId(resourceId).map(mockDataMapper::toDto);
    }
}
