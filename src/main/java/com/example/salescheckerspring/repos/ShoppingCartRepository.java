package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.ShoppingCart;
import com.example.salescheckerspring.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {

List<ShoppingCart> findByOrderedIsFalse ();
List<ShoppingCart> findByOrderedIsFalseAndUserIsLike (User user);

}
