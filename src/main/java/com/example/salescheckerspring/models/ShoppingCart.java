package com.example.salescheckerspring.models;

import javax.persistence.*;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long productId;
    private String productName;
    private int quantity;
    private float amount;
    private boolean ordered;

    private float sumAmount;

    @OneToOne
    private User user;

    public ShoppingCart() {
    }

    public ShoppingCart(long productId, String productName, int quantity, float amount,float sumAmount) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
        this.sumAmount = sumAmount;
    }

    public ShoppingCart(long productId, String productName, int quantity, float amount, User user) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
        this.user = user;
    }

    public ShoppingCart(long productId, String productName, int quantity, float amount, boolean ordered) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
        this.ordered = ordered;
    }

    public ShoppingCart(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(float sumAmount) {
        this.sumAmount = sumAmount;
    }

    @Override
    public String toString() {
        return
                "Cikkszám:= " + productId + "\n" +
                        "Termék név:= " + productName + "\n" +
                        "Mennyiség:= " + quantity + "\n" +
                        "Összeg:= " + amount + "\n" +
                        "Összesen:= " + sumAmount;
    }
}