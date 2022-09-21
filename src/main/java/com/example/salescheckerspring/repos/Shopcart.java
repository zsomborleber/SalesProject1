package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.ProductPast;
import com.example.salescheckerspring.models.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Shopcart extends CrudRepository<ShoppingCart, Long> {



}
