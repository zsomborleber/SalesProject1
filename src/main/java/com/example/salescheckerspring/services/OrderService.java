package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.Order;
import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.repos.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> listAll(String searching) {
        if (searching != null) {
            return repository.findAll(searching);
        }
        return repository.findAll();
    }
}
