package com.innovasoftware.mockapi.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class ResourceMapperTest {

    private ResourceMapper resourceMapper;

    @BeforeEach
    public void setUp() {
        resourceMapper = new ResourceMapperImpl();
    }
}
