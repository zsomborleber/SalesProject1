package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.ProductPast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPastRepository extends CrudRepository<ProductPast, Long> {

    List<ProductPast> findProductPastByYear(int year);


}
