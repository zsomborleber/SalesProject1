package com.example.salescheckerspring.DTO;

import javax.persistence.Column;

public class UserDto {
    private Long id;
    private String companyName;
    private String address;
    private String email;
    private Long taxNumber;
    private boolean enabled;
    private float discount;

    public UserDto() {
    }

    public UserDto(Long id, String companyName, String address, String email, Long taxNumber, boolean enabled, float discount) {
        this.id = id;
        this.companyName = companyName;
        this.address = address;
        this.email = email;
        this.taxNumber = taxNumber;
        this.enabled = enabled;
        this.discount = discount;
    }

    public UserDto(String email, float discount) {
        this.email = email;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(Long taxNumber) {
        this.taxNumber = taxNumber;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
