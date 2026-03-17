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

    @Test
    void testStandardType() {
        // Standard Type: /** @var User */ for $user → should return User.
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new MockVariable("$user", "User");
        PhpType result = resolver.inferTypeFromDoc(variable);
        assertEquals("User", result.toString());
    }

    @Test
    void testUnionType() {
        // Union Type: /** @var string|int */ for $id → should return a UnionType of string and int.
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new MockVariable("$id", "string|int");
        PhpType result = resolver.inferTypeFromDoc(variable);
        assertEquals("string|int", result.toString());
    }

    @Test
    void testNameMismatch() {
        // Name Mismatch: /** @var Admin $adm */ for variable $guest → should return mixed.
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new MockVariable("$guest", "Admin $adm");
        PhpType result = resolver.inferTypeFromDoc(variable);
        assertEquals("mixed", result.toString());
    }

    @Test
    void testFallbackNoDocBlock() {
        // Fallback: If no DocBlock exists -> mixed.
        TypeResolver resolver = new TypeResolver();
        PhpVariable variable = new PhpVariable() {
            @Override public String getName() { return "$any"; }
            @Override public PhpDocBlock getDocBlock() { return null; }
        };
        PhpType result = resolver.inferTypeFromDoc(variable);
        assertEquals("mixed", result.toString());
    }

    @Test
    void testMultipleTags() {
        // Multiple Tags: If a DocBlock has /** @var int $id */ and /** @var string $name */, return string for $name.
        TypeResolver resolver = new TypeResolver();
        List<String> tags = Arrays.asList("int $id", "string $name");
        PhpVariable variable = new MockMultiVariable("$name", tags);

        PhpType result = resolver.inferTypeFromDoc(variable);
        assertEquals("string", result.toString());
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
