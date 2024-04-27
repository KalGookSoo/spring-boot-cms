package com.kalgooksoo.core.bulk.command;

public record CommandWrapper<Command>(CommandType type, Command command) {

}
