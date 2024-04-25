package com.kalgooksoo.cms.config;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;

@Configuration
public class ApplicationRunner implements CommandLineRunner, ApplicationListener<ApplicationStartedEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    public ApplicationRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        logger.info("Application started");
    }

    @Override
    public void run(String... args) {
        createAdmin();
        createUser();
    }

    private void createUser() {
        CreateUserCommand createUserCommand = new CreateUserCommand(
                "tester",
                "12341234",
                "테스터",
                "miro3721",
                "gmail.com",
                "010",
                "1234",
                "5678"
        );
        try {
            userService.createUser(createUserCommand);
        } catch (DataIntegrityViolationException e) {
            logger.info("계정이 이미 존재합니다");
        }
    }

    private void createAdmin() {
        CreateUserCommand createAdminCommand = new CreateUserCommand(
                "admin",
                "12341234",
                "관리자",
                "ga.miro3721",
                "gmail.com",
                "010",
                "1234",
                "5678"
        );
        try {
            userService.createAdmin(createAdminCommand);
        } catch (DataIntegrityViolationException e) {
            logger.info("계정이 이미 존재합니다");
        }
    }

}
