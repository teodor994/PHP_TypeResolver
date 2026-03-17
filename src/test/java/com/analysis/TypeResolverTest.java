package com.analysis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

class TypeResolverTest {

    @Test
    void testNamedTagMatch() {
        TypeResolver resolver = new TypeResolver();

        PhpVariable variable = new MockVariable("$log", "Logger $log");

        PhpType result = resolver.inferTypeFromDoc(variable);

        assertEquals("Logger", result.toString());
    }

    static class MockVariable implements PhpVariable {
        private String name;
        private String tagValue;

        MockVariable(String name, String tagValue) {
            this.name = name;
            this.tagValue = tagValue;
        }

        @Override public String getName() { return name; }
        @Override public PhpDocBlock getDocBlock() {
            return tagName -> Collections.singletonList(() -> tagValue);
        }
    }


    static class MockMultiVariable implements PhpVariable {
        private String name;
        private List<String> tagValues;

        MockMultiVariable(String name, List<String> tagValues) {
            this.name = name;
            this.tagValues = tagValues;
        }

        @Override public String getName() { return name; }
        @Override public PhpDocBlock getDocBlock() {
            return tagName -> tagValues.stream()
                    .map(val -> (DocTag) () -> val)
                    .collect(Collectors.toList());
        }
    }
}
