package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.ResourceSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Spring Data MongoDB repository for the ResourceSchema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceSchemaRepository extends MongoRepository<ResourceSchema, String> {
    Set<ResourceSchema> findAllByResourceId(String resourceId);

    void deleteAllByResourceId(String resourceId);
}
