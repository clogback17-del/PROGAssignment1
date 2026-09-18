/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.progassignment1;

import java.util.Scanner;

/**
 *
 * @author letlhogonolokgosiemang
 */
public class PROGAssignment1 {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();
        
        System.out.println("======= Welcome to the Registration System =====");
        
        // registration info
        
        System.out.println("Please enter your First Name:");
        String firstName = scanner.nextLine();
        
        System.out.println("Please enter your Last Name:");
        String lastName = scanner.nextLine();
        
        System.out.println("Please enter your Username (it must contain '_' and be 5 characters long):");
        String username = scanner.nextLine();
        
        System.out.println("Enter your password (must be a minimum of 8 character, 1 capital letter, 1 number, 1 special character)");
        String password = scanner.nextLine();
        
        System.out.println("Please enter a South African Phone Number (e.g. +27834567891):");
        String cellPhone = scanner.nextLine();
        
        String registrationMessage = loginSystem.registerUser(username, password, cellPhone, firstName, lastName);
        System.out.println("/n" + registrationMessage);
          
        
        //Login info
        
        System.out.println("/n==== Login ====");
        System.out.println("Please enter your Username: ");
        String loginusername = scanner.nextLine();
        
        System.out.println("Pleasse enter your password: ");
        String loginPassword = scanner.nextLine();
        
        boolean isLoggedIn = loginSystem.loginuser(loginusername, loginPassword);
        String loginStatusMessage = loginSystem.returnLoginStatus(isLoggedIn, firstName, lastName);
         
          System.out.println(loginStatusMessage);

        
        scanner.close();
        
    }
}
