package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Resource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceRepository extends MongoRepository<Resource, String> {
    List<Resource> findByProjectId(String projectId);

}
