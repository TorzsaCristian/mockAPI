package com.innovasoftware.mockapi.web.rest;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.innovasoftware.mockapi.service.*;
import com.innovasoftware.mockapi.service.dto.*;
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

    private final MockDataGeneratorService mockDataGeneratorService;

    @Autowired
    MockDataService mockDataService;

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceSchemaService resourceSchemaService;

    @Autowired
    ProjectService projectService;

    public MockDataResource(MockDataGeneratorService mockDataGeneratorService) {
        this.mockDataGeneratorService = mockDataGeneratorService;
    }

    @PostMapping("/mock-data/project/{projectId}")
    public ResponseEntity<ResourceDTO> createMock(@PathVariable(value = "projectId", required = false) final String projectId,
                                                  @RequestBody ResourcePayloadDTO resourcePayloadDTO) throws URISyntaxException {
        log.info("REST request to create resourcePayloadDTO : {}", resourcePayloadDTO);

        ResourceDTO resource = this.handleSave(projectId, resourcePayloadDTO);

        MockDataDTO mockData = mockDataService.findByResourceId(resource.getId()).orElse(new MockDataDTO());
        mockData.setResource(resource);
        mockData.setData(mockDataGeneratorService.generateMockData(resource));
        mockDataService.save(mockData);


        return ResponseEntity
            .ok(resource);
    }

    @PostMapping("/mock-data/resource/{resourceId}/generate")
    public ResponseEntity<CountDTO> generateMockData(
        @PathVariable(value = "resourceId") final String resourceId,
        @RequestBody CountDTO count) {
        log.info("REST request to generate mock data for resource : {} with count : {}", resourceId, count.getCount());

        ResourceDTO resource = resourceService.findOne(resourceId).orElseThrow();
        resource.setCount(count.getCount());
        resource = resourceService.save(resource);

        MockDataDTO mockData = mockDataService.findByResourceId(resourceId).orElse(new MockDataDTO());
        mockData.setResource(resource);
        mockData.setData(mockDataGeneratorService.generateMockData(resource));
        mockDataService.save(mockData);

        return ResponseEntity
            .ok(count);
    }

    @GetMapping("/mock-data/{resourceId}")
    public ResponseEntity<String> createMock(@PathVariable(value = "resourceId") final String resourceId) {
        MockDataDTO mockData = mockDataService.findByResourceId(resourceId).orElseThrow();
        return ResponseEntity.ok(mockData.getData());
    }

    @PutMapping("/mock-data/{resourceId}")
    public ResponseEntity<String> updateMockData(@PathVariable(value = "resourceId") final String resourceId, @RequestBody String mockDataJSON) {
        if(!isValidJson(mockDataJSON)){
            return ResponseEntity.badRequest().body("Invalid JSON");
        }
        ResourceDTO resource = resourceService.findOne(resourceId).orElseThrow();

        MockDataDTO mockData = mockDataService.findByResourceId(resourceId).orElseThrow();
        mockData.setData(mockDataJSON);
        mockData.setResource(resource);
        mockData = mockDataService.save(mockData);
        return ResponseEntity.ok(mockData.getData());
    }


    private ResourceDTO handleSave(String projectId, ResourcePayloadDTO mockData) {

        ProjectDTO project = projectService.findOne(projectId).orElseThrow();

        ResourceDTO resource;
        if (mockData.getId() == null) {
            resource = new ResourceDTO();
            resource.setName(mockData.getName());
            resource.setProject(project);
            resource.setCount(0);
            resource = resourceService.save(resource);
        } else {
            resource = resourceService.findOne(mockData.getId()).orElse(new ResourceDTO());
            resource.setResourceSchemas(new HashSet<>());
            resourceSchemaService.deleteByResourceId(resource.getId());
        }

        Set<ResourceSchemaDTO> resourceSchemaDTOSet = new HashSet<>();

        ResourceDTO finalResource = resource;
        mockData.getResourceSchema().forEach(resourceSchemaDTO -> {
            resourceSchemaDTO.setResource(finalResource);
            ResourceSchemaDTO resourceSchema = resourceSchemaService.save(resourceSchemaDTO);
            resourceSchemaDTOSet.add(resourceSchema);
        });

        resource.setResourceSchemas(resourceSchemaDTOSet);
        resource = resourceService.save(resource);

        return resource;
    }

    public static boolean isValidJson(String jsonInString) {
        try {
            JsonParser.parseString(jsonInString);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }
}



