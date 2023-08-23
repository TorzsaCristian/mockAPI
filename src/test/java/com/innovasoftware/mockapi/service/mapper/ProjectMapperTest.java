package com.innovasoftware.mockapi.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class ProjectMapperTest {

    private ProjectMapper projectMapper;

    @BeforeEach
    public void setUp() {
        projectMapper = new ProjectMapperImpl();
    }
}
