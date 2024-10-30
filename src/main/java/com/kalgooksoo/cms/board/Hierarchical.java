package com.kalgooksoo.cms.board;

import java.util.List;

public interface Hierarchical<T extends Hierarchical<T, ID>, ID> {

    ID getId();

    ID getParentId();

    List<T> getChildren();

    void setChildren(List<T> children);

    default boolean isRoot() {
        return getParentId() == null;
    }

    default boolean hasParent() {
        return !isRoot();
    }

}
