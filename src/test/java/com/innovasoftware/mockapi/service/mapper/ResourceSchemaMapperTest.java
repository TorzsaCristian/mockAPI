package com.innovasoftware.mockapi.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class ResourceSchemaMapperTest {

    private ResourceSchemaMapper resourceSchemaMapper;

    @BeforeEach
    public void setUp() {
        resourceSchemaMapper = new ResourceSchemaMapperImpl();
    }
}
