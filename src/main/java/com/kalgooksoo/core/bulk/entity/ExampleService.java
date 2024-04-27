package com.kalgooksoo.core.bulk.entity;

import com.kalgooksoo.core.bulk.command.Command;
import com.kalgooksoo.core.bulk.command.CommandWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExampleService {

    private final ExampleRepository repository;

    public ExampleService(ExampleRepository repository) {
        this.repository = repository;
    }

    // Transactional
    public void execute(List<CommandWrapper<Command<String>>> commandWrappers) {
        List<Command<String>> commandsByCreate = new ArrayList<>();
        List<Command<String>> commandsByUpdate = new ArrayList<>();
        List<String> commandsByDelete = new ArrayList<>();

        for(CommandWrapper<Command<String>> commandWrapper : commandWrappers) {
            switch (commandWrapper.type()) {
                case CREATE:
                    commandsByCreate.add(commandWrapper.command());
                    break;
                case UPDATE:
                    commandsByUpdate.add(commandWrapper.command());
                    break;
                case DELETE:
                    // Assuming payload ID is fetchable via getId()
                    commandsByDelete.add(commandWrapper.command().getId());
                    break;
            }
        }

        if (!commandsByCreate.isEmpty()) {
            List<ExampleEntity> createdEntities = commandsByCreate.stream()
                    .map(ExampleEntity::create)
                    .toList();

            repository.saveAll(createdEntities);
        }

        if (!commandsByUpdate.isEmpty()) {
            List<String> ids = commandsByUpdate.stream()
                    .map(Command::getId)
                    .collect(Collectors.toList());

            Map<String, ExampleEntity> existingEntities =
                    repository.findAllById(ids)
                            .stream()
                            .collect(Collectors.toMap(ExampleEntity::getId, Function.identity()));

            List<ExampleEntity> updatedEntities = commandsByUpdate.stream()
                    .map(command -> {
                        ExampleEntity existing = existingEntities.get(command.getId());
                        // Assuming a method setting fields from another ExampleEntity exists
                        return existing.update(command);
                    })
                    .toList();
            repository.saveAll(updatedEntities);
        }

        if (!commandsByDelete.isEmpty()) {
            repository.deleteAllById(commandsByDelete);
        }
    }
}
