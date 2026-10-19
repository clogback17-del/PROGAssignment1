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
    private static final String CELL_PATTERN = "^\\+\\d{1,3}\\d{1,10}$";
    
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
     
   public static boolean isValidRecipient(String number) {
    return number != null && number.matches(CELL_PATTERN);
}

public static String recipientStatus(String number) {
    if (isValidRecipient(number)) {
        return "Cell phone number successfully captured.";
    }
    return "Cell phone number is incorrectly formatted or does not contain an international code. "
            + "Please correct the number and try again.";
}

public String checkRecipientCell() {
    return recipientStatus(recipient);
}  
    
     
     //message must be 250 characters or less
     public String checkMessageLength() {
    return lengthStatus(messageText);
}

public static String lengthStatus(String text) {
    int length = text == null ? 0 : text.length();
    if (length <= MAX_LENGTH) {
        return "Message ready to send.";
    }
    return "Message exceeds 250 characters by "
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
            try {
                storeMessage();
                return "Message successfully stored.";
            } catch (IOException e) {
                return "Message could not be stored: " + e.getMessage();
            }

        default:
            return "Invalid option.";
        }
}
        
    
    
    public static int returnTotalMessages() {
    return totalMessagesSent;
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

// Writes all stored messages to storedMessages.json
public void storeMessage() throws IOException {
    storedMessages.add(this);

    try (FileWriter writer = new FileWriter(JSON_FILE)) {
        writer.write("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            writer.write("  {\n");
            writer.write("    \"messageID\": \"" + escapeJson(m.messageID) + "\",\n");
            writer.write("    \"messageHash\": \"" + escapeJson(m.messageHash) + "\",\n");
            writer.write("    \"recipient\": \"" + escapeJson(m.recipient) + "\",\n");
            writer.write("    \"message\": \"" + escapeJson(m.messageText) + "\"\n");
            writer.write(i < storedMessages.size() - 1 ? "  },\n" : "  }\n");
        }
        writer.write("]\n");
    }
}

// Makes quotes and slashes safe inside JSON text
private static String escapeJson(String text) {
    return text.replace("\\", "\\\\")
               .replace("\"", "\\\"")
               .replace("\n", "\\n");
}
    
}
