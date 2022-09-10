package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findAllByEmail(String email);
}
