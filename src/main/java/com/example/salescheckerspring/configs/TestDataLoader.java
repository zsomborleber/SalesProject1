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
        User user = new User("Gyula KFT.", "kiscica", "Budapest", "Gyula@sales.com", 12345678L, Roles.USER);
        User admin = new User("Tulajdonos", "admin", "Budapest", "admin", 1234578L, Roles.ADMIN);
        userService.saveUser(user);
        userService.saveUser(admin);

    }

}


