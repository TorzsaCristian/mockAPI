package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.MockData;
import com.innovasoftware.mockapi.service.dto.MockDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link MockData} and its DTO {@link MockDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface MockDataMapper extends EntityMapper<MockDataDTO, MockData> {
    @Mapping(target = "resource", source = "resource", ignore = true)
    MockDataDTO toDto(MockData s);

    MockData toEntity(MockDataDTO projectDTO);
}
