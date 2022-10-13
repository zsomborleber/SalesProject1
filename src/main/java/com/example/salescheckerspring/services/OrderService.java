package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.Order;
import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.ShoppingCart;
import com.example.salescheckerspring.models.User;
import com.example.salescheckerspring.repos.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private OrderRepository repository;
    private UserService service;

    public OrderService(OrderRepository repository,UserService service) {
        this.repository = repository;
        this.service = service;
    }

    public List<Order> listAll(String searching,User user) {
        if (searching != null) {
            List<Order> completedList = new ArrayList<>();
            List<Order> order = repository.findAll(searching);
            for (Order x: order){
                if (x.getCustomer().getId().equals(user.getId())){
                    completedList.add(x);
                }
            }
            return completedList;
        }
        return repository.findAllByUserIs(user);
    }

}
