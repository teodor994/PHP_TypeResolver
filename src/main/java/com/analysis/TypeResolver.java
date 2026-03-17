package com.analysis;

import java.util.ArrayList;
import java.util.List;

public class TypeResolver {

    private PhpType resolveComplexType(String typeStr) {
        if (typeStr.contains("|")) {
            String[] parts = typeStr.split("\\|");
            List<PhpType> types = new ArrayList<>();
            for (String p : parts) {
                types.add(TypeFactory.createType(p));
            }
            return TypeFactory.createUnionType(types);
        }
        return TypeFactory.createType(typeStr);
    }

    public PhpType inferTypeFromDoc(PhpVariable variable) {
        PhpDocBlock x = variable.getDocBlock();
        String nameVar = variable.getName();
        if (x == null) {
            return TypeFactory.createType("mixed");
        }

        List<DocTag> tags = x.getTagsByName("var");

        for (DocTag tag : tags) {
            DocTag t = tag;
            String values = t.getValue().trim();
            if (values.isEmpty()) continue;
            if (values.contains(nameVar)) {
                String typeVar = values.replace(nameVar, "");
                typeVar = typeVar.trim();
                if (typeVar.isEmpty()) {
                    return TypeFactory.createType("mixed");
                }
                return resolveComplexType(typeVar);
            } else if (!values.contains("$")) {
                return resolveComplexType(values);
            }
        }

        return TypeFactory.createType("mixed");
    }
}