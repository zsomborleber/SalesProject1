package com.example.salescheckerspring.models;

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
    private int price;
    private String supplier;
    private int year;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Product() {
    }

    public Product(Long articleNumber, long EANCode, String articleName, int quantity, int price, String supplier, int year) {
        this.articleNumber = articleNumber;
        this.EANCode = EANCode;
        this.articleName = articleName;
        this.quantity = quantity;
        this.price = price;
        this.supplier = supplier;
        this.year = year;
    }



    public Long getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(Long articleNumber) {
        this.articleNumber = articleNumber;
    }

    public long getEANCode() {
        return EANCode;
    }

    public void setEANCode(long EANCode) {
        this.EANCode = EANCode;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
