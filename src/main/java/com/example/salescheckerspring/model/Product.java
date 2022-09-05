package com.example.salescheckerspring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private  Long articleNumber;
    private  long EANCode;
    private  String articleName;
    private int quantity;
    private int value;
    private String supplier;

    public Product() {
    }

    public Product(Long articleNumber, long EANCode, String articleName, int quantity, int value, String supplier) {
        this.articleNumber = articleNumber;
        this.EANCode = EANCode;
        this.articleName = articleName;
        this.quantity = quantity;
        this.value = value;
        this.supplier = supplier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getArticleNumber() {
        return articleNumber;
    }

    public long getEANCode() {
        return EANCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Termék{" +
                "Cikkszám: " + articleNumber + " ; " +
                "EAN kód: " + EANCode + " ; " +
                "Cikk név: " + articleName + " ; " +
                "Mennyiség: " + quantity + " ; " +
                "Bizonylat érték: " + value + " ; " +
                "Beszállító: " + supplier + " ; " +
                '}' ;
    }

}

