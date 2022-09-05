package com.example.salescheckerspring.repository;

import com.example.salescheckerspring.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByIdGreaterThanOrderByValueAsc(long id);

    List<Product> findAllByOrderByValueAsc();

    List<Product> findByIdGreaterThanOrderByValueDesc(long id);

    List<Product> findByIdGreaterThanOrderByQuantityAsc(long id);

    List<Product> findByIdGreaterThanOrderByQuantityDesc(long id);
}
