package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository <Order,Long> {
}
