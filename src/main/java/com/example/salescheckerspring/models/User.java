package com.example.salescheckerspring.models;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class User  implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
       private String companyName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String address;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(nullable = false,length = 10,unique = true)
    private Long taxNumber;

    private boolean enabled;
    @Column(name = "verification_code")
    private String verificationCode;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User() {
    }

    private Roles role;

    public User(String companyName, String password, String address, String email, Long taxNumber) {
        this.companyName = companyName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.taxNumber = taxNumber;
    }

    public User(String companyName, String password, String address, String email, Long taxNumber, boolean enabled) {
        this.companyName = companyName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.taxNumber = taxNumber;
        this.enabled = enabled;
    }
    public User(String companyName, String password, String address, String email, Long taxNumber, Roles role, boolean enabled) {
        this.companyName = companyName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.taxNumber = taxNumber;
        this.role = role;
        this.enabled = enabled;
    }

    public User(String companyName, String password, String address, String email, Long taxNumber, Roles role, boolean enabled, String verificationCode) {
        this.companyName = companyName;
        this.password = password;
        this.address = address;
        this.email = email;
        this.taxNumber = taxNumber;
        this.role = role;
        this.enabled = enabled;
        this.verificationCode = verificationCode;
    }




    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    public String getFullName() {
        return this.companyName;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(role.name()));

        return list;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
