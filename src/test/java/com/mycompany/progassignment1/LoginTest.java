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
      
      //password test
      
      @Test
      public void testPasswordMeetsComplexity(){
          assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
      }
      
      @Test
      public void testPasswordCoesNotMeetComplexity(){
          assertFalse(login.checkPasswordComplexity("password"));
      }
      
      //Cellphone test
      
      @Test
      public void testCellphoneCorrectlyFormatted(){
          assertTrue(login.checkCellPhoneNumber("+2783896876"));
      }
      
      @Test 
      public void testCellPhoneIncorreclyFormatted(){
          assertFalse(login.checkCellPhoneNumber("08966553"));
      }
      
      //Registration message
      
      @Test
      public void testRegisterUserSuccess(){
          String expected = "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
        assertEquals(expected,
                login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith"));
      }
      
      @Test
      public void testRegisterUserBadUsernameMessage(){
          String expected = "Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.";
        assertEquals(expected,
                login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith"));
      }
      
       @Test
    public void testRegisterUserBadPasswordMessage() {
        String expected = "Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, "
                + "and a special character.";
        assertEquals(expected,
                login.registerUser("kyl_1", "password", "+27838968976", "Kyle", "Smith"));
    }
 
    @Test
    public void testRegisterUserBadCellPhoneMessage() {
        String expected = "Cell phone number incorrectly formatted or does not contain "
                + "international code.";
        assertEquals(expected,
                login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith"));
    }
    
    // login test
    
    
    
    public LoginTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
}
