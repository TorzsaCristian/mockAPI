package com.innovasoftware.mockapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.innovasoftware.mockapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EndpointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endpoint.class);
        Endpoint endpoint1 = new Endpoint();
        endpoint1.setId("id1");
        Endpoint endpoint2 = new Endpoint();
        endpoint2.setId(endpoint1.getId());
        assertThat(endpoint1).isEqualTo(endpoint2);
        endpoint2.setId("id2");
        assertThat(endpoint1).isNotEqualTo(endpoint2);
        endpoint1.setId(null);
        assertThat(endpoint1).isNotEqualTo(endpoint2);
    }
}
