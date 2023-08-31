package com.innovasoftware.mockapi.service.dto;


import java.util.List;
import java.util.Set;

public class MockDataDTO extends ResourceDTO {

    private Set<ResourceSchemaDTO> resourceSchema;
    private List<EndpointDTO> endpoints;

    public Set<ResourceSchemaDTO> getResourceSchema() {
        return resourceSchema;
    }

    public void setResourceSchema(Set<ResourceSchemaDTO> resourceSchema) {
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
