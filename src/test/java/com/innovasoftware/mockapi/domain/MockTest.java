package com.innovasoftware.mockapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.innovasoftware.mockapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mock.class);
        Mock mock1 = new Mock();
        mock1.setId("id1");
        Mock mock2 = new Mock();
        mock2.setId(mock1.getId());
        assertThat(mock1).isEqualTo(mock2);
        mock2.setId("id2");
        assertThat(mock1).isNotEqualTo(mock2);
        mock1.setId(null);
        assertThat(mock1).isNotEqualTo(mock2);
    }
}
