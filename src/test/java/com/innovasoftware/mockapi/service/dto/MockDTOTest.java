package com.innovasoftware.mockapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.innovasoftware.mockapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MockDTO.class);
        MockDTO mockDTO1 = new MockDTO();
        mockDTO1.setId("id1");
        MockDTO mockDTO2 = new MockDTO();
        assertThat(mockDTO1).isNotEqualTo(mockDTO2);
        mockDTO2.setId(mockDTO1.getId());
        assertThat(mockDTO1).isEqualTo(mockDTO2);
        mockDTO2.setId("id2");
        assertThat(mockDTO1).isNotEqualTo(mockDTO2);
        mockDTO1.setId(null);
        assertThat(mockDTO1).isNotEqualTo(mockDTO2);
    }
}
