package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.Resource;
import com.innovasoftware.mockapi.domain.ResourceSchema;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import com.innovasoftware.mockapi.service.dto.ResourceSchemaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceSchema} and its DTO {@link ResourceSchemaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceSchemaMapper extends EntityMapper<ResourceSchemaDTO, ResourceSchema> {
    @Mapping(target = "resource", source = "resource", qualifiedByName = "resourceId", ignore = true)
    ResourceSchemaDTO toDto(ResourceSchema s);

    @Named("resourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResourceDTO toDtoResourceId(Resource resource);
}
