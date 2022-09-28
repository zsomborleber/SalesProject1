package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.Order;
import com.example.salescheckerspring.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository <Order,Long> {

    List<Order> findAllByUserIs(User user);
}
