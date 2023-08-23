package com.innovasoftware.mockapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.innovasoftware.mockapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceSchemaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceSchema.class);
        ResourceSchema resourceSchema1 = new ResourceSchema();
        resourceSchema1.setId("id1");
        ResourceSchema resourceSchema2 = new ResourceSchema();
        resourceSchema2.setId(resourceSchema1.getId());
        assertThat(resourceSchema1).isEqualTo(resourceSchema2);
        resourceSchema2.setId("id2");
        assertThat(resourceSchema1).isNotEqualTo(resourceSchema2);
        resourceSchema1.setId(null);
        assertThat(resourceSchema1).isNotEqualTo(resourceSchema2);
    }
}
