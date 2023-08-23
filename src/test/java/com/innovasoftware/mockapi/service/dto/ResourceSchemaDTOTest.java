package com.innovasoftware.mockapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.innovasoftware.mockapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceSchemaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceSchemaDTO.class);
        ResourceSchemaDTO resourceSchemaDTO1 = new ResourceSchemaDTO();
        resourceSchemaDTO1.setId("id1");
        ResourceSchemaDTO resourceSchemaDTO2 = new ResourceSchemaDTO();
        assertThat(resourceSchemaDTO1).isNotEqualTo(resourceSchemaDTO2);
        resourceSchemaDTO2.setId(resourceSchemaDTO1.getId());
        assertThat(resourceSchemaDTO1).isEqualTo(resourceSchemaDTO2);
        resourceSchemaDTO2.setId("id2");
        assertThat(resourceSchemaDTO1).isNotEqualTo(resourceSchemaDTO2);
        resourceSchemaDTO1.setId(null);
        assertThat(resourceSchemaDTO1).isNotEqualTo(resourceSchemaDTO2);
    }
}
