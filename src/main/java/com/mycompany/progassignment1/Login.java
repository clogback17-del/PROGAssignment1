/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignment1;

import java.util.regex.Pattern;

/**
 *
 * @author letlhogonolokgosiemang
 */
class Login {
   
    
    //store user inf
    String storedUsername;
    String storePassword;
    String storeCellPhone;
    String storeFirstName;
    String storeLastName;
    private String storedPassword;
    private String storedCellPhone;
    private String storedFirstName;
    private String storedLastName;
    
    //check if username contain an underscore and is no more tha 5 characters long
    
    public boolean checkUserName(String username) {
       
        if (username == null || username.isEmpty()){
             return false;
        }
        
        if (!username.contains("_")){
             return false;
        }
        if (username.length() >5){
             return false;
        }
         return true;
    }
    
    
    
    
    //check password complexity rules
    public boolean checkPasswordComplexity (String password){
        
        //check min length
        
        if (password == null || password.length()<8){
            return false;
        }
        
        boolean hasCapital = false;
        boolean hasNumber = false; 
        boolean hasSpecial = false;
        
       
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            
            
            //check capital letter
            if (Character.isUpperCase(c)){
                hasCapital =true ;
            }
                
                
                //check number
                else if (Character.isDigit(c)) {
                        
                        hasNumber = true;
                }
                
                else if (!Character.isLetterOrDigit(c)) {
                        hasSpecial = true;
                }
            
            
            if (hasCapital && hasNumber && hasSpecial){
                break;
            }
        }
       return hasCapital && hasNumber && hasSpecial; 
       
    }
    
    // 4. Check SA cell phone number (Regex required by rubric)
    public boolean checkCellPhoneNumber(String cellPhone) {
        // Regex: Starts with +27, followed by 9 digits
        String regex = "^\\+27[0-9]{9}$"; 
        return Pattern.matches(regex, cellPhone);
    }
    
    
           // Registers the user and returns the appropriate message
    public String registerUser(String username, String password, String cellPhone, String firstName, String lastName) {
        // Check Username
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        //  Check Password
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        
        //Check Cell Phone
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // If all checks pass, store the details so we can use them for login later
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellPhone = cellPhone;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;
        // Return success message
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

     public boolean loginuser(String loginusername, String loginPassword) {
         
        if (this.storedUsername == null || this.storedPassword == null){
         return false;}
        return false;
        
        
   
     }

    String returnLoginStatus(boolean loggedIn, String firstName, String lastName) {
      
        return null;
      
    }
       
    
    
   
}
