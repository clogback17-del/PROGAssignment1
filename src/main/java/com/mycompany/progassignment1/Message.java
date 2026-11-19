package com.mycompany.progassignment1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author letlhogonolokgosiemang
 */

// The Message class represents one QuickChat message and keeps track of all messages sent while the program is running.
//
// JSON CODE ATTRIBUTION
// Library: Gson by Google (turns Java objects into JSON text and JSON text back into Java objects).
// [1] Google, "Gson User Guide," GitHub. [Online].
//     Available: https://github.com/google/gson/blob/main/UserGuide.md [Accessed: 19-Nov-2026].
public class Message {

    public static final int MAX_LENGTH = 250;
    public static final String JSON_FILE = "storedMessages.json";

    // The file that stored messages are saved to (tests change this so they do not touch the real file)
    private static String jsonFile = JSON_FILE;
    private static final String CELL_PATTERN = "^\\+\\d{1,3}\\d{1,10}$";

    // shared across all messages while the program runs
    private static final List<Message> sentMessage = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    // normal constructor generates a random 10 digit message ID
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    public static String generateMessageID() {
        Random random = new Random();
        StringBuilder digits = new StringBuilder();

        while (digits.length() < 10) {
            digits.append(random.nextInt(10));
        }
        return digits.toString().substring(0, 10);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String getMessageIDStatus() {
        return "Message ID generated: " + messageID;
    }

    // hash = first two digits of the ID : message number : first word + last word, in capitals
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

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

    // message must be 250 characters or less
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

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        switch (choice) {
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

    public static void setJsonFile(String fileName) {
        jsonFile = fileName;
    }

    // Adds THIS message to the JSON file. The old messages are read first so they are kept, not overwritten.
    public void storeMessage() throws IOException {
        Message[] existing = readStoredMessages(jsonFile);
        Message[] updated = Arrays.copyOf(existing, existing.length + 1);   // one extra slot at the end
        updated[existing.length] = this;
        writeStoredMessages(updated, jsonFile);
    }

    // Reads the JSON file into an array of Message objects (gson.fromJson: JSON text -> Java objects)
    public static Message[] readStoredMessages(String fileName) throws IOException {
        try (FileReader reader = new FileReader(fileName)) {
            Message[] messages = new Gson().fromJson(reader, Message[].class);
            if (messages == null) {
                return new Message[0];          // the file exists but is empty
            }
            return messages;
        } catch (FileNotFoundException e) {
            return new Message[0];              // nothing has been stored yet
        }
    }

    // Writes an array of messages to the JSON file (gson.toJson: Java objects -> JSON text)
    public static void writeStoredMessages(Message[] messages, String fileName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(messages, writer);
        }
    }
}