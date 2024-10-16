package com.kalgooksoo.cms.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Hierarchical<T extends Hierarchical<T>> {

    T getParent();

    List<T> getChildren();

    void setChildren(List<T> children);

    void moveTo(T parent);

    default boolean isRoot() {
        return getParent() == null;
    }

    default boolean hasParent() {
        return !isRoot();
    }

    default T mapChildren(T parentNode, Map<T, List<T>> nodesGroupByParent) {
        List<T> children = nodesGroupByParent.getOrDefault(parentNode, new ArrayList<>());
        List<T> nestedChildren = children.stream()
                .map(child -> mapChildren(child, nodesGroupByParent))
                .toList();
        parentNode.setChildren(nestedChildren);
        return parentNode;
    }

    static <T extends Hierarchical<T>> List<Hierarchical<T>> build(List<T> elements) {
        Map<T, List<T>> collect = elements.stream()
                .filter(T::hasParent)
                .collect(Collectors.groupingBy(T::getParent));

        return elements.stream()
                .filter(T::isRoot)
                .map(element -> element.mapChildren(element, collect))
                .collect(Collectors.toList());
    }

}
