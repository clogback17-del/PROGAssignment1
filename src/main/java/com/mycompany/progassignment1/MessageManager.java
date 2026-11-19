package com.mycompany.progassignment1;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

/**
 * PART 3: MessageManager keeps every message in ARRAYS and reports on them.
 *
 * The first six arrays are PARALLEL: the same index belongs to the same message.
 * The three flag arrays hold the message text of the sent, disregarded and stored messages.
 * The stored messages are read from the JSON file with Gson (see the attribution in Message.java).
 */
public class MessageManager {

    public static final int MAX_MESSAGES = 50;

    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARD = "Disregard";

    // ---- Parallel arrays: ALL messages ----
    private final String[] messageIDs = new String[MAX_MESSAGES];
    private final String[] messageHashes = new String[MAX_MESSAGES];
    private final String[] recipients = new String[MAX_MESSAGES];
    private final String[] messageTexts = new String[MAX_MESSAGES];
    private final String[] flags = new String[MAX_MESSAGES];
    private final int[] messageNumbers = new int[MAX_MESSAGES];
    private int messageCount = 0;

    // ---- One array per flag (message text only) ----
    private final String[] sentMessages = new String[MAX_MESSAGES];
    private int sentCount = 0;
    private final String[] disregardedMessages = new String[MAX_MESSAGES];
    private int disregardedCount = 0;
    private final String[] storedMessages = new String[MAX_MESSAGES];
    private int storedCount = 0;

    // Puts a message into the arrays. The counters are the next free index. Returns false if the arrays are full.
    public boolean addMessage(Message message, String flag) {
        if (messageCount >= MAX_MESSAGES) {
            return false;
        }
        messageIDs[messageCount] = message.getMessageID();
        messageHashes[messageCount] = message.getMessageHash();
        recipients[messageCount] = message.getRecipient();
        messageTexts[messageCount] = message.getMessageText();
        messageNumbers[messageCount] = message.getMessageNumber();
        flags[messageCount] = flag;
        messageCount++;

        if (flag.equals(FLAG_SENT)) {
            sentMessages[sentCount] = message.getMessageText();
            sentCount++;
        } else if (flag.equals(FLAG_DISREGARD)) {
            disregardedMessages[disregardedCount] = message.getMessageText();
            disregardedCount++;
        } else if (flag.equals(FLAG_STORED)) {
            storedMessages[storedCount] = message.getMessageText();
            storedCount++;
        }
        return true;
    }

    // Reads the JSON file INTO the arrays: every message in the file is added with the flag "Stored"
    public String loadStoredFromJson(String fileName) {
        try {
            Message[] fromFile = Message.readStoredMessages(fileName);
            int loaded = 0;
            for (int i = 0; i < fromFile.length; i++) {
                if (addMessage(fromFile[i], FLAG_STORED)) {
                    loaded++;
                }
            }
            return loaded + " stored message(s) loaded from " + fileName;
        } catch (IOException | JsonSyntaxException e) {
            return "Stored messages could not be read from " + fileName;
        }
    }

    // Writes the messages that are still stored back to the JSON file (used after a delete)
    public String saveStoredToJson(String fileName) {
        Message[] stillStored = new Message[storedCount];
        int next = 0;
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals(FLAG_STORED)) {
                stillStored[next] = new Message(messageIDs[i], messageNumbers[i], recipients[i], messageTexts[i]);
                next++;
            }
        }
        try {
            Message.writeStoredMessages(stillStored, fileName);
            return "Stored messages file updated.";
        } catch (IOException e) {
            return "Could not update " + fileName;
        }
    }

    // a) The sender and recipient of every stored message
    public String displaySenderAndRecipient(String sender) {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals(FLAG_STORED)) {
                result.append("Sender: ").append(sender)
                      .append(" | Recipient: ").append(recipients[i]).append("\n");
            }
        }
        return result.toString();
    }

    // b) The longest stored message
    public String longestStoredMessage() {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        String longest = storedMessages[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return longest;
    }

    // c) Search for a message ID and show its recipient and message
    public String searchByMessageID(String id) {
        for (int i = 0; i < messageCount; i++) {
            if (messageIDs[i].equals(id)) {
                return "Recipient: " + recipients[i] + " | Message: " + messageTexts[i];
            }
        }
        return "Message ID " + id + " not found.";
    }

    // d) All the messages sent or stored for one recipient
    public String searchByRecipient(String recipient) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < messageCount; i++) {
            boolean notDisregarded = !flags[i].equals(FLAG_DISREGARD);
            if (recipients[i].equals(recipient) && notDisregarded) {
                result.append(messageTexts[i]).append("\n");
            }
        }
        if (result.length() == 0) {
            return "No messages found for recipient " + recipient + ".";
        }
        return result.toString();
    }

    // e) Delete a stored message using its hash
    public String deleteByHash(String hash) {
        // Step 1: find the index of the stored message with this hash
        int deleteIndex = -1;
        for (int i = 0; i < messageCount; i++) {
            if (messageHashes[i].equals(hash) && flags[i].equals(FLAG_STORED)) {
                deleteIndex = i;
            }
        }
        if (deleteIndex == -1) {
            return "Message hash " + hash + " not found.";
        }

        String deleted = messageTexts[deleteIndex];

        // Step 2: remove it from the stored array (shift the rest one place left)
        removeFromStoredArray(deleted);

        // Step 3: shift everything after it one place left in EVERY parallel array
        for (int i = deleteIndex; i < messageCount - 1; i++) {
            messageIDs[i] = messageIDs[i + 1];
            messageHashes[i] = messageHashes[i + 1];
            recipients[i] = recipients[i + 1];
            messageTexts[i] = messageTexts[i + 1];
            messageNumbers[i] = messageNumbers[i + 1];
            flags[i] = flags[i + 1];
        }

        // Step 4: clear the last slot and lower the counter
        messageIDs[messageCount - 1] = null;
        messageHashes[messageCount - 1] = null;
        recipients[messageCount - 1] = null;
        messageTexts[messageCount - 1] = null;
        messageNumbers[messageCount - 1] = 0;
        flags[messageCount - 1] = null;
        messageCount--;

        return "Message: \"" + deleted + "\" successfully deleted.";
    }

    // Removes one text from the stored array and closes the gap
    private void removeFromStoredArray(String text) {
        int index = -1;
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].equals(text)) {
                index = i;
            }
        }
        for (int i = index; i < storedCount - 1; i++) {
            storedMessages[i] = storedMessages[i + 1];
        }
        storedMessages[storedCount - 1] = null;
        storedCount--;
    }

    // f) Report with the full details of every stored message
    public String displayStoredReport() {
        return buildReport(FLAG_STORED, "STORED MESSAGES REPORT");
    }

    // The same report for the sent messages
    public String displaySentReport() {
        return buildReport(FLAG_SENT, "SENT MESSAGES REPORT");
    }

    // Builds a report for one flag: Message ID, Message Hash, Recipient, Message
    private String buildReport(String flag, String title) {
        StringBuilder report = new StringBuilder(title + "\n");
        int total = 0;
        for (int i = 0; i < messageCount; i++) {
            if (flags[i].equals(flag)) {
                report.append("----------\n")
                      .append("Message ID: ").append(messageIDs[i]).append("\n")
                      .append("Message Hash: ").append(messageHashes[i]).append("\n")
                      .append("Recipient: ").append(recipients[i]).append("\n")
                      .append("Message: ").append(messageTexts[i]).append("\n");
                total++;
            }
        }
        if (total == 0) {
            return "No " + flag.toLowerCase() + " messages.";
        }
        report.append("----------\nTotal: ").append(total);
        return report.toString();
    }

    // ---- Getters (used by the unit tests) ----

    public int getTotalMessages() {
        return messageCount;
    }

    public int getSentCount() {
        return sentCount;
    }

    public int getDisregardedCount() {
        return disregardedCount;
    }

    public int getStoredCount() {
        return storedCount;
    }

    public String getSentMessage(int index) {
        return sentMessages[index];
    }

    public String getDisregardedMessage(int index) {
        return disregardedMessages[index];
    }

    public String getStoredMessage(int index) {
        return storedMessages[index];
    }

    public String getMessageHash(int index) {
        return messageHashes[index];
    }

    public String getMessageID(int index) {
        return messageIDs[index];
    }
}