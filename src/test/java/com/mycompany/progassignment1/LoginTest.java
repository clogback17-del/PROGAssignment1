/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.progassignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author letlhogonolokgosiemang
 */
public class LoginTest {
    
    private Login login;
    
    @BeforeEach 
    public void setUp(){
        login = new Login();
    }
    
    // username test
    
    @Test
    public void testUsernameCorrectlyFomatted(){
        assertTrue(login.checkUserName("kyl_1"));
    }
        
         @Test
      public void testUsernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    
       
    }
    
    public LoginTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
}
