package com.innovasoftware.mockapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ResourceSchema.
 */
@Document(collection = "resource_schema")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceSchema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("type")
    private String type;

    @Field("faker_method")
    private String fakerMethod;

    @DBRef
    @Field("resource")
    @JsonIgnoreProperties(value = { "resourceSchemas", "endpoints", "mock" }, allowSetters = true)
    private Resource resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ResourceSchema id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ResourceSchema name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public ResourceSchema type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFakerMethod() {
        return this.fakerMethod;
    }

    public ResourceSchema fakerMethod(String fakerMethod) {
        this.setFakerMethod(fakerMethod);
        return this;
    }

    public void setFakerMethod(String fakerMethod) {
        this.fakerMethod = fakerMethod;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ResourceSchema resource(Resource resource) {
        this.setResource(resource);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceSchema)) {
            return false;
        }
        return id != null && id.equals(((ResourceSchema) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceSchema{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", fakerMethod='" + getFakerMethod() + "'" +
            "}";
    }
}
