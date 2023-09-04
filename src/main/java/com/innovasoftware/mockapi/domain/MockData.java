package com.innovasoftware.mockapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document(collection = "mock_data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MockData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("data")
    private String data;

    @DBRef
    @Field("resource")
    @JsonIgnoreProperties(value = { "resourceSchemas", "endpoints", "mock", "data", "mockData" }, allowSetters = true)
    private Resource resource;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public MockData id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getData() {
        return this.data;
    }

    public MockData data(String data) {
        this.setData(data);
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Resource getResource() {
        return this.resource;
    }

    public MockData resource(Resource resource) {
        this.setResource(resource);
        return this;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MockData)) {
            return false;
        }
        return id != null && id.equals(((MockData) o).id);
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
            ", resource=" + getResource().getId() +
            "}";
    }
}
