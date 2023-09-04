package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.MockData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the MockData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MockDataRepository extends MongoRepository<MockData, String> {
    Optional<MockData> findByResourceId(String resourceId);
}
