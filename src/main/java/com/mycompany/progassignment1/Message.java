/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignment1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author letlhogonolokgosiemang
 * 
 * 
 */

//message class represents one Quickchat message and keeps track of all messages sebt while the progam is running 
public class Message {
    
    public static final int MAX_LENGTH = 250;
    public static final String JSON_FILE = "storedMessages.json";
    
    //shared across all messages while the program runs
    
    private static final List<Message>
            sentMessage = new ArrayList<>();
    private static final List<Message>
            storedMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;
    
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    
    //normal constructor generates a random 10 digit message ID
    
    public Message (int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
        
    }
    
    public Message (String messageID, int messageNumber, String recipient, String messageText){
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        
    }
    public static String generateMessageID(){
        Random random = new Random();
        StringBuilder digits = new StringBuilder();
        
        while (digits.length() < 10){
            digits.append(random.nextInt(10));
        }
        return digits.toString().substring(0, 10);
    }
     public boolean checkMessageID(){
         return messageID != null &&
                 messageID.length() <= 10;
     }
     
     public String getMessageIDStatus(){
         return "Message ID generated: " +
                 messageID;
     }
     
     public String createMessageHash(){
         String[] words = 
                 messageText.trim().split("\\s+");
         String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
         
         String lastWord = words[words.length-1].replaceAll("[^a-zA-Z0-9]", "");
         
         String hash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
         
         return hash.toUpperCase();
     }
     
     //Repient must contain the international code; reuese the Part1 regex check
     public String checkRecipientCell() {
         if (Login.checkCellPhoneNumber(recipient)){
             return "Cell phone number successfully captured.";
         }
         return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
     }
     //message must be 250 characters or less
     public String checkMessageLength(){
         int length = messageText.length();
         
         if (length <= MAX_LENGTH){
             return "Message ready to send.";
     }
         return "Message exceeds 250 characters by"
                 + (length - MAX_LENGTH)
                + "; please reduce the size.";
     }
    public String sentMessage (int choice) {
        switch (choice){
            case 1:
            sentMessage.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
                
                case 2:
            return "Press 0 to delete the message.";

        case 3:
            storedMessages.add(this);
    return "Message successfully stored.";

        default:
            return "Invalid option.";
    
        }
    }
    
    public static String printMessages() {
    if (sentMessage.isEmpty()) {
        return "No messages sent yet.";
    }

    StringBuilder output = new StringBuilder();

    for (Message message : sentMessage) {
        output.append(message.getDetails()).append("\n");
    }

    return output.toString().trim();
}
    public static int returnTotalMessages() {
    return totalMessagesSent;
}

public String getDetails() {
    return "Message ID: " + messageID
            + "\nMessage Hash: " + messageHash
            + "\nRecipient: " + recipient
            + "\nMessage: " + messageText + "\n";
}

public static void reset() {
    sentMessage.clear();
    storedMessages.clear();
    totalMessagesSent = 0;
}

public String getMessageID() {
    return messageID;
}

public String getMessageHash() {
    return messageHash;
}

public String getRecipient() {
    return recipient;
}

public String getMessageText() {
    return messageText;
}
public int getMessageNumber() {
    return messageNumber;
} 
    
}
