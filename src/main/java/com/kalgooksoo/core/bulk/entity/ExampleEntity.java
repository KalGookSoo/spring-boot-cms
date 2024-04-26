package com.kalgooksoo.core.bulk.entity;

import com.kalgooksoo.core.bulk.command.Command;

public class ExampleEntity {
    private String id;

    public static ExampleEntity create(Command command) {
        return new ExampleEntity();
    }

    ExampleEntity update(Command command) {
        return this;
    }

    public String getId() {
        return id;
    }
}
