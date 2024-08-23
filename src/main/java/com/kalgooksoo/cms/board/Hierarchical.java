package com.kalgooksoo.cms.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Hierarchical<T extends Hierarchical<T>> {

    T getParent();

    List<T> getChildren();

    void setChildren(List<T> children);

    default boolean isRoot() {
        return getParent() == null;
    }

    default boolean hasParent() {
        return !isRoot();
    }

    void moveTo(T parent);

    default T mapChildren(T parentNode, Map<T, List<T>> nodesGroupByParent) {
        List<T> children = nodesGroupByParent.getOrDefault(parentNode, new ArrayList<>());
        List<T> nestedChildren = children.stream()
                .map(child -> mapChildren(child, nodesGroupByParent))
                .toList();
        parentNode.setChildren(nestedChildren);
        return parentNode;
    }

}
