package com.kalgooksoo.core.hierarchy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface HierarchicalFactory {

    static <T extends Hierarchical<T>> T mapChildren(T parentNode, Map<T, List<T>> nodesGroupByParent) {
        List<T> children = nodesGroupByParent.getOrDefault(parentNode, new ArrayList<>());
        List<T> nestedChildren = children.stream()
                .map(child -> mapChildren(child, nodesGroupByParent))
                .toList();
        nestedChildren.forEach(parentNode::addChild);
        return parentNode;
    }

    static <T extends Hierarchical<T>> List<T> build(List<T> elements) {
        Map<T, List<T>> collect = elements.stream()
                .filter(T::hasParent)
                .collect(Collectors.groupingBy(T::getParent));
        return elements.stream()
                .filter(T::isRoot)
                .map(element -> mapChildren(element, collect))
                .collect(Collectors.toList());
    }

}
