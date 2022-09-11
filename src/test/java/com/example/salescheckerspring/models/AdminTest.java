package com.example.salescheckerspring.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    @Test
    void TestAdminCreate (){
        Admin admin = new Admin("Admin", "Admin", "admin@sales.com"  );
        assertEquals("Admin", admin.getUsername());
        assertEquals("Admin", admin.getPassword());
        assertEquals("admin@sales.com", admin.getEmail());
        assertNull(admin.getId()); // Null az Id ertek.

    }

}