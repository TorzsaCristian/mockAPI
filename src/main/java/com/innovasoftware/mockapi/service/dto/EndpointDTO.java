package com.innovasoftware.mockapi.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.innovasoftware.mockapi.domain.Endpoint} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EndpointDTO implements Serializable {

    private String id;

    @NotNull
    private String url;

    @NotNull
    private String method;

    private Boolean enabled;

    private String response;

    private ResourceDTO resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ResourceDTO getResource() {
        return resource;
    }

    public void setResource(ResourceDTO resource) {
        this.resource = resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EndpointDTO)) {
            return false;
        }

        EndpointDTO endpointDTO = (EndpointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, endpointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EndpointDTO{" +
            "id='" + getId() + "'" +
            ", url='" + getUrl() + "'" +
            ", method='" + getMethod() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", response='" + getResponse() + "'" +
            ", resource=" + getResource() +
            "}";
    }
}
