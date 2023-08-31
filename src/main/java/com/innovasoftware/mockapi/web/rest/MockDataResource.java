package com.innovasoftware.mockapi.web.rest;

import com.github.javafaker.Faker;
import com.innovasoftware.mockapi.service.MockDataService;
import com.innovasoftware.mockapi.service.ProjectService;
import com.innovasoftware.mockapi.service.ResourceSchemaService;
import com.innovasoftware.mockapi.service.ResourceService;
import com.innovasoftware.mockapi.service.dto.MockDataDTO;
import com.innovasoftware.mockapi.service.dto.ProjectDTO;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import com.innovasoftware.mockapi.service.dto.ResourceSchemaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MockDataResource {

    private final Logger log = LoggerFactory.getLogger(MockResource.class);

    private final MockDataService mockDataService;


    private final Faker faker = new Faker();

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceSchemaService resourceSchemaService;

    @Autowired
    ProjectService projectService;

    public MockDataResource(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    @PostMapping("/mock-data/project/{projectId}")
    public ResponseEntity<ResourceDTO> createMock(@PathVariable(value = "projectId", required = false) final String projectId,
                                                  @RequestBody MockDataDTO mockData) throws URISyntaxException {
        log.info("REST request to create MockData : {}", mockData);

        ProjectDTO project = projectService.findOne(projectId).orElseThrow();


        ResourceDTO resource = new ResourceDTO();
        resource.setName(mockData.getName());
        resource.setProject(project);
        resource = resourceService.save(resource);

        Set<ResourceSchemaDTO> resourceSchemaDTOSet = new HashSet<>();

        ResourceDTO finalResource = resource;
        mockData.getResourceSchema().forEach(resourceSchemaDTO -> {
            resourceSchemaDTO.setResource(finalResource);
            ResourceSchemaDTO resourceSchema = resourceSchemaService.save(resourceSchemaDTO);
            resourceSchemaDTOSet.add(resourceSchema);
        });

        resource.setResourceSchemas(resourceSchemaDTOSet);
        resource.setCount(0);
        resource = resourceService.save(resource);



//        mockDataService.generateMockData(mockData);


        return ResponseEntity
            .ok(resource);
    }
}



