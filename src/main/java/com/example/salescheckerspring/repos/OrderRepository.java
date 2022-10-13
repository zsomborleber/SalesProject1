package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.Order;
import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository <Order,Long> {

    List<Order> findAllByUserIs(User user);

    List<Order> findAllByIsCompletedIsFalse();

    List<Order> findAllByIsCompletedIsTrue();
    @Query("select p from Order p where " + " concat(p.id,p.localDate) " + "like %?1%")
    List<Order> findAll(String keyword);
    List<Order> findAll();

}
