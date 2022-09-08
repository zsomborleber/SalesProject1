package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.ProductPast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPastRepository extends CrudRepository<ProductPast, Long> {
}
