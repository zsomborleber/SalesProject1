package com.example.salescheckerspring.services;

import com.example.salescheckerspring.models.Order;
import com.example.salescheckerspring.models.ShoppingCart;
import com.example.salescheckerspring.repos.ShoppingCartRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;

    private UserService userService;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserService userService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
    }


    public void setTrueAfterOrdered() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByOrderedIsFalseAndUserIsLike(userService.getLoggedInUser());
        for (ShoppingCart x : shoppingCarts) {
            x.setOrdered(true);
        }
        shoppingCartRepository.saveAll(shoppingCarts);
    }
    public float ShoppingCartSumOrderedAmount() {
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findByOrderedIsFalseAndUserIsLike(userService.getLoggedInUser());
        float sumAmount = 0;
        for (ShoppingCart x : shoppingCarts) {
            sumAmount += x.getAmount();
        }
        return sumAmount;
    }
   
}
