package com.analysis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TypeFactory {
    public static PhpType createType(String typeName) {
        return new SimplePhpType(typeName);
    }

    public static UnionType createUnionType(List<PhpType> types) {
        return new PhpUnionType(types);
    }


    private static class SimplePhpType implements PhpType {
        private final String name;

        SimplePhpType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimplePhpType)) return false;
            SimplePhpType that = (SimplePhpType) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private static class PhpUnionType implements UnionType {
        private final List<PhpType> types;

        PhpUnionType(List<PhpType> types) {
            this.types = types;
        }

        @Override
        public String toString() {
            return types.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("|"));
        }
    }
}
