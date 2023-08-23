package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.ResourceSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ResourceSchema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceSchemaRepository extends MongoRepository<ResourceSchema, String> {}
