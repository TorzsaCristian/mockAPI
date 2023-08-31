package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.Mock;
import com.innovasoftware.mockapi.domain.Project;
import com.innovasoftware.mockapi.domain.Resource;
import com.innovasoftware.mockapi.domain.ResourceSchema;
import com.innovasoftware.mockapi.service.dto.MockDTO;
import com.innovasoftware.mockapi.service.dto.ProjectDTO;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import com.innovasoftware.mockapi.service.dto.ResourceSchemaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring", uses={ResourceSchemaMapper.class})
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {
    @Mapping(target = "mock", source = "mock", qualifiedByName = "mockId")
    @Mapping(target = "project", source = "project", qualifiedByName = "projectId")
//    @Mapping(target = "resourceSchema", source = "resourceSchema", qualifiedByName = "resourceSchemaId")
    ResourceDTO toDto(Resource s);

    @Named("mockId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MockDTO toDtoMockId(Mock mock);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);

    @Named("resourceSchemaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResourceSchemaDTO toDtoResourceSchemaId(ResourceSchema resourceSchema);
}
