package com.innovasoftware.mockapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Project.
 */
@Document(collection = "project")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("resource_tree_id")
    private String resourceTreeId;

    @NotNull
    @Field("name")
    private String name;

    @Field("prefix")
    private String prefix;

    @Field("is_public")
    private Boolean isPublic;

    @Field("created_at")
    private ZonedDateTime createdAt;

    @DBRef
    @Field("owner")
    private User owner;

    @DBRef
    @Field("resources")
    @JsonIgnoreProperties(value = { "resourceSchemas", "endpoints", "mock", "project" }, allowSetters = true)
    private Set<Resource> resources = new HashSet<>();

    @DBRef
    @Field("collaborators")
    private Set<User> collaborators = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Project id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceTreeId() {
        return this.resourceTreeId;
    }

    public Project resourceTreeId(String resourceTreeId) {
        this.setResourceTreeId(resourceTreeId);
        return this;
    }

    public void setResourceTreeId(String resourceTreeId) {
        this.resourceTreeId = resourceTreeId;
    }

    public String getName() {
        return this.name;
    }

    public Project name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Project prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public Project isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Project createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Project owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setResources(Set<Resource> resources) {
        if (this.resources != null) {
            this.resources.forEach(i -> i.setProject(null));
        }
        if (resources != null) {
            resources.forEach(i -> i.setProject(this));
        }
        this.resources = resources;
    }

    public Project resources(Set<Resource> resources) {
        this.setResources(resources);
        return this;
    }

    public Project addResources(Resource resource) {
        this.resources.add(resource);
        resource.setProject(this);
        return this;
    }

    public Project removeResources(Resource resource) {
        this.resources.remove(resource);
        resource.setProject(null);
        return this;
    }

    public Set<User> getCollaborators() {
        return this.collaborators;
    }

    public void setCollaborators(Set<User> users) {
        this.collaborators = users;
    }

    public Project collaborators(Set<User> users) {
        this.setCollaborators(users);
        return this;
    }

    public Project addCollaborators(User user) {
        this.collaborators.add(user);
        return this;
    }

    public Project removeCollaborators(User user) {
        this.collaborators.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", resourceTreeId='" + getResourceTreeId() + "'" +
            ", name='" + getName() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
