package com.innovasoftware.mockapi.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.innovasoftware.mockapi.domain.Mock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MockDTO implements Serializable {

    private String id;

    private String prefix;

    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MockDTO)) {
            return false;
        }

        MockDTO mockDTO = (MockDTO) o;
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
            ", prefix='" + getPrefix() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
