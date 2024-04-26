package com.kalgooksoo.core.bulk.entity;

import java.util.List;

public interface ExampleRepository {
    void saveAll(Iterable<ExampleEntity> entities);
    List<ExampleEntity> findAllById(Iterable<String> ids);
    void deleteAllById(Iterable<? extends String> ids);
}
