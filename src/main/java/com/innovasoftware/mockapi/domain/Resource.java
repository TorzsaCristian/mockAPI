package com.innovasoftware.mockapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Resource.
 */
@Document(collection = "resource")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("generator")
    private String generator;

    @Field("count")
    private Integer count;

    @DBRef
    @Field("resourceSchema")
    @JsonIgnoreProperties(value = { "resource" }, allowSetters = true)
    private Set<ResourceSchema> resourceSchemas = new HashSet<>();

    @DBRef
    @Field("endpoints")
    @JsonIgnoreProperties(value = { "resource" }, allowSetters = true)
    private Set<Endpoint> endpoints = new HashSet<>();

    @DBRef
    @Field("mock")
    @JsonIgnoreProperties(value = { "resources" }, allowSetters = true)
    private Mock mock;

    @DBRef
    @Field("project")
    @JsonIgnoreProperties(value = { "owner", "resources", "collaborators" }, allowSetters = true)
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Resource id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Resource name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenerator() {
        return this.generator;
    }

    public Resource generator(String generator) {
        this.setGenerator(generator);
        return this;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public Integer getCount() {
        return this.count;
    }

    public Resource count(Integer count) {
        this.setCount(count);
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<ResourceSchema> getResourceSchemas() {
        return this.resourceSchemas;
    }

    public void setResourceSchemas(Set<ResourceSchema> resourceSchemas) {
        if (this.resourceSchemas != null) {
            this.resourceSchemas.forEach(i -> i.setResource(null));
        }
        if (resourceSchemas != null) {
            resourceSchemas.forEach(i -> i.setResource(this));
        }
        this.resourceSchemas = resourceSchemas;
    }

    public Resource resourceSchemas(Set<ResourceSchema> resourceSchemas) {
        this.setResourceSchemas(resourceSchemas);
        return this;
    }

    public Resource addResourceSchema(ResourceSchema resourceSchema) {
        this.resourceSchemas.add(resourceSchema);
        resourceSchema.setResource(this);
        return this;
    }

    public Resource removeResourceSchema(ResourceSchema resourceSchema) {
        this.resourceSchemas.remove(resourceSchema);
        resourceSchema.setResource(null);
        return this;
    }

    public Set<Endpoint> getEndpoints() {
        return this.endpoints;
    }

    public void setEndpoints(Set<Endpoint> endpoints) {
        if (this.endpoints != null) {
            this.endpoints.forEach(i -> i.setResource(null));
        }
        if (endpoints != null) {
            endpoints.forEach(i -> i.setResource(this));
        }
        this.endpoints = endpoints;
    }

    public Resource endpoints(Set<Endpoint> endpoints) {
        this.setEndpoints(endpoints);
        return this;
    }

    public Resource addEndpoints(Endpoint endpoint) {
        this.endpoints.add(endpoint);
        endpoint.setResource(this);
        return this;
    }

    public Resource removeEndpoints(Endpoint endpoint) {
        this.endpoints.remove(endpoint);
        endpoint.setResource(null);
        return this;
    }

    public Mock getMock() {
        return this.mock;
    }

    public void setMock(Mock mock) {
        this.mock = mock;
    }

    public Resource mock(Mock mock) {
        this.setMock(mock);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Resource project(Project project) {
        this.setProject(project);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", generator='" + getGenerator() + "'" +
            ", count=" + getCount() +
            "}";
    }
}
