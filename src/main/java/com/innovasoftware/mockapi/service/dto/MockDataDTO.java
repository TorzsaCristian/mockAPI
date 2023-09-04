package com.innovasoftware.mockapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.innovasoftware.mockapi.domain.Mock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MockDataDTO implements Serializable {

    private String id;

    private String data;

    private ResourceDTO resourceDTO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ResourceDTO getResource() {
        return resourceDTO;
    }

    public void setResource(ResourceDTO resourceDTO) {
        this.resourceDTO = resourceDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MockDataDTO)) {
            return false;
        }

        MockDataDTO mockDTO = (MockDataDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MockDTO{" +
            "id='" + getId() + "'" +
            ", resource='" + getResource()+ "'" +
            "}";
    }
}
