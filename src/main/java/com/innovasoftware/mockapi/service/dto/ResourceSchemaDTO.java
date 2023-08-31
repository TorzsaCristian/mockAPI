package com.innovasoftware.mockapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.innovasoftware.mockapi.domain.ResourceSchema} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceSchemaDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    private String fakerMethod;

    private ResourceDTO resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFakerMethod() {
        return fakerMethod;
    }

    public void setFakerMethod(String fakerMethod) {
        this.fakerMethod = fakerMethod;
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
        if (!(o instanceof ResourceSchemaDTO)) {
            return false;
        }

        ResourceSchemaDTO resourceSchemaDTO = (ResourceSchemaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceSchemaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceSchemaDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", fakerMethod='" + getFakerMethod() + "'" +
            ", resource=" + getResource() +
            "}";
    }
}
