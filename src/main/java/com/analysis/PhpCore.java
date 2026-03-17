package com.analysis;

import java.util.List;

interface PhpType {}
interface UnionType extends PhpType {}

interface DocTag {
    String getValue();
}

interface PhpDocBlock {
    List<DocTag> getTagsByName(String tagName);
}

interface PhpVariable {
    PhpDocBlock getDocBlock();
    String getName();
}
