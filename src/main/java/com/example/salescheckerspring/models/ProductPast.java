package com.example.salescheckerspring.models;

public class ProductPast {

    private Long id;

    private  Long articleNumber;

    private  long EANCode;

    private  String articleName;

    private int quantity;

    private int value;

    private String supplier;

    private int year;

    public ProductPast(Long id) {
        this.id = id;
    }
    public ProductPast (){}


    public ProductPast(Long articleNumber, long EANCode, String articleName, int quantity, int value, String supplier, int year) {
        this.articleNumber = articleNumber;
        this.EANCode = EANCode;
        this.articleName = articleName;
        this.quantity = quantity;
        this.value = value;
        this.supplier = supplier;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
