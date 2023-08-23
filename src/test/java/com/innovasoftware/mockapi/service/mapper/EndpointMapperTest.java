package com.innovasoftware.mockapi.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class EndpointMapperTest {

    private EndpointMapper endpointMapper;

    @BeforeEach
    public void setUp() {
        endpointMapper = new EndpointMapperImpl();
    }
}
