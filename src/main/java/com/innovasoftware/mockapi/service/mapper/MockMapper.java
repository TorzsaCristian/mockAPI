package com.innovasoftware.mockapi.service.mapper;

import com.innovasoftware.mockapi.domain.Mock;
import com.innovasoftware.mockapi.service.dto.MockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mock} and its DTO {@link MockDTO}.
 */
@Mapper(componentModel = "spring")
public interface MockMapper extends EntityMapper<MockDTO, Mock> {}
