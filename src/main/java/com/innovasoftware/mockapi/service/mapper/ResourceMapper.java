package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.Mock;
import com.innovasoftware.mockapi.domain.Resource;
import com.innovasoftware.mockapi.service.dto.MockDTO;
import com.innovasoftware.mockapi.service.dto.ResourceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {
    @Mapping(target = "mock", source = "mock", qualifiedByName = "mockId")
    ResourceDTO toDto(Resource s);

    @Named("mockId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MockDTO toDtoMockId(Mock mock);
}
