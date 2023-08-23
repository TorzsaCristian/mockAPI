package com.innovasoftware.mockapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Endpoint.
 */
@Document(collection = "endpoint")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Endpoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("url")
    private String url;

    @NotNull
    @Field("method")
    private String method;

    @Field("enabled")
    private Boolean enabled;

    @Field("response")
    private String response;

    @DBRef
    @Field("resource")
    @JsonIgnoreProperties(value = { "resourceSchemas", "endpoints", "mock" }, allowSetters = true)
    private Resource resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Endpoint id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public Endpoint url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return this.method;
    }

    public Endpoint method(String method) {
        this.setMethod(method);
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Endpoint enabled(Boolean enabled) {
        this.setEnabled(enabled);
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getResponse() {
        return this.response;
    }

    public Endpoint response(String response) {
        this.setResponse(response);
        return this;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Resource getResource() {
        return this.resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Endpoint resource(Resource resource) {
        this.setResource(resource);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Endpoint)) {
            return false;
        }
        return id != null && id.equals(((Endpoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Endpoint{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", method='" + getMethod() + "'" +
            ", enabled='" + getEnabled() + "'" +
            ", response='" + getResponse() + "'" +
            "}";
    }
}
