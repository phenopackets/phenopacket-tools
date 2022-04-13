package org.phenopackets.phenotools.builder.builders;

import org.junit.jupiter.api.Test;
import org.phenopackets.schema.v2.core.File;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FIleBuilderTest {

    @Test
    public void testUri() {
        String uri = "http://example.com/foobar";
        File file = FileBuilder.builder(uri).build();
        assertEquals(uri, file.getUri());
    }

    @Test
    public void testHg37() {
        String uri = "http://example.com/foobar";
        File file = FileBuilder.hg37vcf(uri).build();
        assertEquals(2, file.getFileAttributesCount());
        Map<String, String> attributes = file.getFileAttributesMap();
        assertTrue(attributes.containsKey("genomeAssembly"));
        assertEquals("GRCh37", attributes.get("genomeAssembly"));
        assertTrue(attributes.containsKey("fileFormat"));
        assertEquals("VCF", attributes.get("fileFormat"));
    }
}
