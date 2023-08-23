package com.innovasoftware.mockapi.repository;

import com.innovasoftware.mockapi.domain.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Project entity.
 */
@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    @Query("{}")
    Page<Project> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Project> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Project> findOneWithEagerRelationships(String id);
}
