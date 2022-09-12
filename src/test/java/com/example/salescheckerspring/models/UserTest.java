package com.example.salescheckerspring.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void TestUserCreate() {
        User user = new User("user","user","Budapest","sales@sales.com", 12345678L);
        assertEquals("user", user.getCompanyName());
        assertEquals("user", user.getPassword());
        assertEquals("Budapest", user.getAddress());
        assertEquals("sales@sales.com", user.getEmail());
        assertEquals(12345678L, user.getTaxNumber());
        assertNull(user.getId());

    }
}