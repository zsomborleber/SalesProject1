package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.ShoppingCart;
import com.example.salescheckerspring.repos.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public void setTrueAfterOrdered(){
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByOrderedIsFalse();
        for (ShoppingCart x: shoppingCarts){
            x.setOrdered(true);
        }
        shoppingCartRepository.saveAll(shoppingCarts);
    }
}
