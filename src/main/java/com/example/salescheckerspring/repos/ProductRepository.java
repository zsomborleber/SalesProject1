package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository <Product, Long> {
}
