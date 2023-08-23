package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.Endpoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Endpoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EndpointRepository extends MongoRepository<Endpoint, String> {}
