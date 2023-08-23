package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.Mock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Mock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MockRepository extends MongoRepository<Mock, String> {}
