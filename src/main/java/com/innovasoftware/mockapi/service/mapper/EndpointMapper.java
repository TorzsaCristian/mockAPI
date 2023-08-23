package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.Endpoint;
import com.innovasoftware.mockapi.domain.Resource;
import com.innovasoftware.mockapi.service.dto.EndpointDTO;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Endpoint} and its DTO {@link EndpointDTO}.
 */
@Mapper(componentModel = "spring")
public interface EndpointMapper extends EntityMapper<EndpointDTO, Endpoint> {
    @Mapping(target = "resource", source = "resource", qualifiedByName = "resourceId")
    EndpointDTO toDto(Endpoint s);

    @Named("resourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResourceDTO toDtoResourceId(Resource resource);
}
