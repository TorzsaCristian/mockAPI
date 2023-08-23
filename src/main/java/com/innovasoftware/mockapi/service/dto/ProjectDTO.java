package com.innovasoftware.mockapi.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.innovasoftware.mockapi.domain.Project} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProjectDTO implements Serializable {

    private String id;

    private String resourceTreeId;

    @NotNull
    private String name;

    private String prefix;

    private Boolean isPublic;

    private ZonedDateTime createdAt;

    private UserDTO owner;

    private Set<UserDTO> collaborators = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceTreeId() {
        return resourceTreeId;
    }

    public void setResourceTreeId(String resourceTreeId) {
        this.resourceTreeId = resourceTreeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public Set<UserDTO> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set<UserDTO> collaborators) {
        this.collaborators = collaborators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectDTO)) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectDTO{" +
            "id='" + getId() + "'" +
            ", resourceTreeId='" + getResourceTreeId() + "'" +
            ", name='" + getName() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", owner=" + getOwner() +
            ", collaborators=" + getCollaborators() +
            "}";
    }
}
