package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.ProductPast;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository <Product, Long> {

    List<Product> findAll();

    List<Product> findByEANCode(long EANcode);
}
