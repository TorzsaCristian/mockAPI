package com.innovasoftware.mockapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.innovasoftware.mockapi.domain.Resource} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    private String generator;

    private Integer count;

    private MockDTO mock;

    private ProjectDTO project;

    private Set<ResourceSchemaDTO> resourceSchemas = new HashSet<>();

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

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public MockDTO getMock() {
        return mock;
    }

    public void setMock(MockDTO mock) {
        this.mock = mock;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public Set<ResourceSchemaDTO> getResourceSchemas() {
        return this.resourceSchemas;
    }

    public void setResourceSchemas(Set<ResourceSchemaDTO> resourceSchemas) {
        this.resourceSchemas = resourceSchemas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceDTO)) {
            return false;
        }

        ResourceDTO resourceDTO = (ResourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", generator='" + getGenerator() + "'" +
            ", count=" + getCount() +
            ", mock=" + getMock() +
            ", project=" + getProject() +
            "}";
    }
}
