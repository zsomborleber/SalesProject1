package com.example.salescheckerspring.Form;

import org.springframework.stereotype.Component;

public class NewPasswordForm {
    private String currentpassword;
    private String newpassword1;
    private String newpassword2;

    public NewPasswordForm(String currentpassword, String newpassword1, String newpassword2) {
        this.currentpassword = currentpassword;
        this.newpassword1 = newpassword1;
        this.newpassword2 = newpassword2;
    }

    public NewPasswordForm() {
    }

    public String getCurrentpassword() {
        return currentpassword;
    }

    public void setCurrentpassword(String currentpassword) {
        this.currentpassword = currentpassword;
    }

    public String getNewpassword1() {
        return newpassword1;
    }

    public void setNewpassword1(String newpassword1) {
        this.newpassword1 = newpassword1;
    }

    public String getNewpassword2() {
        return newpassword2;
    }

    public void setNewpassword2(String newpassword2) {
        this.newpassword2 = newpassword2;
    }
}
