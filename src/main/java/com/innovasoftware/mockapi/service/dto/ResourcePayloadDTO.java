package com.innovasoftware.mockapi.service.dto;


import java.util.List;
import java.util.Set;

public class ResourcePayloadDTO extends ResourceDTO {
    private String id;
    private Set<ResourceSchemaDTO> resourceSchema;
    private List<EndpointDTO> endpoints;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

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
