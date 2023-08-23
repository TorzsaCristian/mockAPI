package com.innovasoftware.mockapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.innovasoftware.mockapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EndpointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EndpointDTO.class);
        EndpointDTO endpointDTO1 = new EndpointDTO();
        endpointDTO1.setId("id1");
        EndpointDTO endpointDTO2 = new EndpointDTO();
        assertThat(endpointDTO1).isNotEqualTo(endpointDTO2);
        endpointDTO2.setId(endpointDTO1.getId());
        assertThat(endpointDTO1).isEqualTo(endpointDTO2);
        endpointDTO2.setId("id2");
        assertThat(endpointDTO1).isNotEqualTo(endpointDTO2);
        endpointDTO1.setId(null);
        assertThat(endpointDTO1).isNotEqualTo(endpointDTO2);
    }
}
