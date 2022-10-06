package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.ShoppingCart;
import com.example.salescheckerspring.repos.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public float getSumValue(List<ShoppingCart> cartItems){
        float sum = 0;
        for (ShoppingCart x : cartItems){
            sum+=x.getAmount();
        }
        return sum;
    }
}
