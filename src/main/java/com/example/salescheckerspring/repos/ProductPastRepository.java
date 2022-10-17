package com.example.salescheckerspring.repos;

import com.example.salescheckerspring.models.Product;
import com.example.salescheckerspring.models.ProductPast;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPastRepository extends CrudRepository<ProductPast, Long> {

    List<ProductPast> findProductPastByYear(Long year);

    List<ProductPast> findAll();

    @Query("select p from ProductPast p where " + "CONCAT(p.articleNumber,p.articleName,p.EANCode,p.supplier) " + "LIKE %?1%" )
    List<ProductPast> findAll(String keyword);

}
