package com.innovasoftware.mockapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Mock.
 */
@Document(collection = "mock")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("prefix")
    private String prefix;

    @Field("version")
    private Integer version;

    @DBRef
    @Field("resources")
    @JsonIgnoreProperties(value = { "resourceSchemas", "endpoints", "mock", "project" }, allowSetters = true)
    private Set<Resource> resources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Mock id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Mock prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getVersion() {
        return this.version;
    }

    public Mock version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setResources(Set<Resource> resources) {
        if (this.resources != null) {
            this.resources.forEach(i -> i.setMock(null));
        }
        if (resources != null) {
            resources.forEach(i -> i.setMock(this));
        }
        this.resources = resources;
    }

    public Mock resources(Set<Resource> resources) {
        this.setResources(resources);
        return this;
    }

    public Mock addResources(Resource resource) {
        this.resources.add(resource);
        resource.setMock(this);
        return this;
    }

    public Mock removeResources(Resource resource) {
        this.resources.remove(resource);
        resource.setMock(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mock)) {
            return false;
        }
        return id != null && id.equals(((Mock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mock{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
