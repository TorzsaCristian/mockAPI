package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.Project;
import com.innovasoftware.mockapi.domain.User;
import com.innovasoftware.mockapi.service.dto.ProjectDTO;
import com.innovasoftware.mockapi.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "collaborators", source = "collaborators", qualifiedByName = "userIdSet")
    ProjectDTO toDto(Project s);

    @Mapping(target = "removeCollaborators", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
