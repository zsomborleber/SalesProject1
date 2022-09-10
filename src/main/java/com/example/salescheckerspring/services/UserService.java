package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.repos.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
      userRepository.save(user);
    }
}
