package com.innovasoftware.mockapi.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class MockMapperTest {

    private MockMapper mockMapper;

    @BeforeEach
    public void setUp() {
        mockMapper = new MockMapperImpl();
    }
}
