package com.innovasoftware.mockapi.web.rest;

import com.github.javafaker.Faker;
import com.innovasoftware.mockapi.service.MockDataService;
import com.innovasoftware.mockapi.service.dto.MockDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class MockDataResource {

    private final Logger log = LoggerFactory.getLogger(MockResource.class);

    private final MockDataService mockDataService;


    private final Faker faker = new Faker();

    public MockDataResource(MockDataService mockDataService) {
        this.mockDataService = mockDataService;
    }

    @PostMapping("/mock-data")
    public ResponseEntity<MockDataDTO> createMock(@RequestBody MockDataDTO mockData) throws URISyntaxException {
        log.info("REST request to create MockData : {}", mockData);



        mockDataService.generateMockData(mockData);


        return ResponseEntity
            .ok(mockData);
    }
}



