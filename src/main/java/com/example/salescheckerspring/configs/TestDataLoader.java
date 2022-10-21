package com.example.salescheckerspring.configs;

import com.example.salescheckerspring.models.Roles;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


//Teszt emberke
@Component
public class TestDataLoader implements ApplicationRunner {
    UserService userService;

    public TestDataLoader(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) {
        User user = new User("Gyula KFT.", "1aA!1aA!1", "Budapest", "pointofsales2022@gmail.com", 12345678L, Roles.USER,true);
            User admin = new User("Tulajdonos", "1aA!1aA!1", "Budapest", "admin", 1234578L, Roles.ADMIN,true);
        userService.saveUserReg(user);
        userService.saveUserReg(admin);

    }

}



