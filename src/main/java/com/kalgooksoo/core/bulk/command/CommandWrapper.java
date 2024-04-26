package com.kalgooksoo.core.bulk.command;

public interface CommandWrapper<Command> {
    CommandType getType();
    Command getCommand();
}
