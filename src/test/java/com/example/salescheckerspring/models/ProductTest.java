package com.example.salescheckerspring.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void TestProduct() {
        Product product = new Product(123456L,12345678L,"product",12,120,"Gyula",2022);
        assertEquals(123456L, product.getArticleNumber());
        assertEquals(12345678L, product.getEANCode());
        assertEquals("product", product.getArticleName());
        assertEquals(12, product.getQuantity());
        assertEquals(120, product.getPrice());
        assertEquals("Gyula", product.getSupplier());
        assertEquals(2022, product.getYear());
    }

}