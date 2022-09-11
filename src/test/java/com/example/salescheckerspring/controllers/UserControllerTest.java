package com.example.salescheckerspring.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Ha nem adja meg a csrf tokenjet akkor el lessz utasitva.
    @Test
    void login_without_csrf() throws Exception {
        String username = "TestService";
        mockMvc.perform(
                        post("/login")
                                .contentType("multipart/form-data")
                                .content("username:" + username + "\n" +
                                        "password:admin"))
                .andExpect(status().is(403));
    }

    /// Nem futnak le. Bad credentials issue.

  /*  @Test
    void login_as_user() throws Exception {
        mockMvc.perform(
                        SecurityMockMvcRequestBuilders.formLogin().user("Gyula@sales.com").password("kiscica"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    } */
   @Test
    void login_with_invalid_credentials() throws Exception {
        mockMvc.perform(
                        SecurityMockMvcRequestBuilders.formLogin().user("Gyula@sales.com").password("kiscicaa"))
                .andExpect(status().is(302));

        mockMvc.perform(
                        SecurityMockMvcRequestBuilders.formLogin().user("aaa").password("aaa"))
                .andExpect(status().is(302));
    }
 }
