package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {

    Optional<User> findByEmail(String email);
    @Query("UPDATE User c SET c.enabled = true where c.id = ?1")
    @Modifying
    public void enable(Long Id);
    @Query("SELECT c FROM User c WHERE c.verificationCode = ?1")
    User findByVerificationCode(String code);

    User findByTaxNumber(Long taxNumber);

}

