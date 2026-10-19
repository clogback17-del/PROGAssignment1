/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignment1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author letlhogonolokgosiemang
 */
public class MessageTest {
    
   private static final String ID_1 = "0012345678";
    private static final String CELL_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";

    private static final String ID_2 = "1234567890";
    private static final String CELL_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    @BeforeEach
    void setUp() {
        Message.reset();
    }

    @Test
    void messageLengthSuccess() {
        Message message = new Message(ID_1, 0, CELL_1, TEXT_1);
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    void messageLengthFailure() {
        Message message = new Message(ID_1, 0, CELL_1, "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                message.checkMessageLength());
    }

    @Test
    void recipientNumberCorrectlyFormatted() {
        Message message = new Message(ID_1, 0, CELL_1, TEXT_1);
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    void recipientNumberIncorrectlyFormatted() {
        Message message = new Message(ID_2, 1, CELL_2, TEXT_2);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.", message.checkRecipientCell());
    }

    @Test
    void messageHashIsCorrect() {
        Message message = new Message(ID_1, 0, CELL_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    void messageIDIsCreated() {
        Message message = new Message(0, CELL_1, TEXT_1);
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertEquals("Message ID generated: " + message.getMessageID(), message.getMessageIDStatus());
    }

    @Test
    void userSelectsSendMessage() {
        Message message = new Message(ID_1, 0, CELL_1, TEXT_1);
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    void userSelectsDisregardMessage() {
        Message message = new Message(ID_2, 1, CELL_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    void userSelectsStoreMessage() throws IOException {
        Message message = new Message(ID_1, 0, CELL_1, TEXT_1);
        assertEquals("Message successfully stored.", message.sentMessage(3));

        String json = Files.readString(Path.of(Message.JSON_FILE));
        assertTrue(json.contains("00:0:HITONIGHT"));
        assertTrue(json.contains(TEXT_1));
    }

    @Test
    void totalMessagesOnlyCountsSentMessages() {
        new Message(ID_1, 0, CELL_1, TEXT_1).sentMessage(1);   // sent
        new Message(ID_2, 1, CELL_2, TEXT_2).sentMessage(2);   // disregarded
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void printMessagesShowsDetailsInOrder() {
        new Message(ID_1, 0, CELL_1, TEXT_1).sentMessage(1);
        String expected = "Message ID: " + ID_1
                + "\nMessage Hash: 00:0:HITONIGHT"
                + "\nRecipient: " + CELL_1
                + "\nMessage: " + TEXT_1 + "\n\n";
        assertEquals(expected, Message.printMessages());
    }  
    
}
