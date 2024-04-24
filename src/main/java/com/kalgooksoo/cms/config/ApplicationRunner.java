package com.kalgooksoo.cms.config;

import com.kalgooksoo.cms.user.command.CreateUserCommand;
import com.kalgooksoo.cms.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;

@Configuration
public class ApplicationRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    public ApplicationRunner(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
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
            userService.createAdmin(createAdminCommand);
        } catch (DataIntegrityViolationException e) {
            logger.info("계정이 이미 존재합니다");
        }
        try {
            userService.createUser(createUserCommand);
        } catch (DataIntegrityViolationException e) {
            logger.info("계정이 이미 존재합니다");
        }

    }

}
