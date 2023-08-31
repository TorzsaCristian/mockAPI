package com.innovasoftware.mockapi.service.dto;


import java.util.List;

public class MockDataDTO extends ResourceDTO {

    private List<ResourceSchemaDTO> resourceSchema;
    private List<EndpointDTO> endpoints;

    public List<ResourceSchemaDTO> getResourceSchema() {
        return resourceSchema;
    }

    public void setResourceSchema(List<ResourceSchemaDTO> resourceSchema) {
        this.resourceSchema = resourceSchema;
    }

    public List<EndpointDTO> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<EndpointDTO> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        return "MockDataDTO{" +
            "resourceSchema=" + resourceSchema +
            ", endpoints=" + endpoints +
            '}';
    }
}
