package com.mycompany.progassignment1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// PART 3 tests: the arrays and the Stored Messages features
class MessageManagerTest {

    // ---- Test data from the PoE ----
    // Message 1: +27834557896, "Did you get the cake?"                                        -> Sent
    // Message 2: +27838884567, "Where are you? You are late! I have asked you to be on time." -> Stored
    // Message 3: +27834484567, "Yohoooo, I am at your gate."                                   -> Disregard
    // Message 4: 0838884567,   "It is dinner time!"                                            -> Sent
    // Message 5: +27838884567, "Ok, I am leaving without you."                                 -> Stored
    // (Message 4's ID is 0838884567 because the PoE searches for that value as a message ID.)
    private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT_5 = "Ok, I am leaving without you.";

    @TempDir
    Path tempDir;

    private MessageManager manager;

    @BeforeEach
    void loadTestData() {
        manager = new MessageManager();
        add("1011121314", 0, "+27834557896", "Did you get the cake?", MessageManager.FLAG_SENT);
        add("2021222324", 1, "+27838884567", TEXT_2, MessageManager.FLAG_STORED);
        add("3031323334", 2, "+27834484567", "Yohoooo, I am at your gate.", MessageManager.FLAG_DISREGARD);
        add("0838884567", 3, "0838884567", "It is dinner time!", MessageManager.FLAG_SENT);
        add("5051525354", 4, "+27838884567", TEXT_5, MessageManager.FLAG_STORED);
    }

    private void add(String id, int number, String recipient, String text, String flag) {
        manager.addMessage(new Message(id, number, recipient, text), flag);
    }

    @Test
    void sentMessagesArrayIsCorrectlyPopulated() {
        assertEquals(2, manager.getSentCount());
        assertEquals("Did you get the cake?", manager.getSentMessage(0));
        assertEquals("It is dinner time!", manager.getSentMessage(1));
    }

    @Test
    void disregardedAndStoredArraysArePopulated() {
        assertEquals(1, manager.getDisregardedCount());
        assertEquals("Yohoooo, I am at your gate.", manager.getDisregardedMessage(0));
        assertEquals(2, manager.getStoredCount());
        assertEquals(TEXT_2, manager.getStoredMessage(0));
        assertEquals(TEXT_5, manager.getStoredMessage(1));
    }

    @Test
    void messageHashAndMessageIdArraysArePopulated() {
        assertEquals(5, manager.getTotalMessages());
        assertEquals("10:0:DIDCAKE", manager.getMessageHash(0));
        assertEquals("20:1:WHERETIME", manager.getMessageHash(1));
        assertEquals("0838884567", manager.getMessageID(3));
    }

    @Test
    void displaySenderAndRecipientOfStoredMessages() {
        String expected = "Sender: Kyle Smith | Recipient: +27838884567\n"
                        + "Sender: Kyle Smith | Recipient: +27838884567\n";
        assertEquals(expected, manager.displaySenderAndRecipient("Kyle Smith"));
    }

    @Test
    void displayTheLongestMessage() {
        assertEquals(TEXT_2, manager.longestStoredMessage());
    }

    @Test
    void searchForMessageId() {
        String result = manager.searchByMessageID("0838884567");
        assertEquals("Recipient: 0838884567 | Message: It is dinner time!", result);
        assertTrue(result.contains("It is dinner time!"));
    }

    @Test
    void searchForMessageIdNotFound() {
        assertEquals("Message ID 0000000000 not found.", manager.searchByMessageID("0000000000"));
    }

    @Test
    void searchAllMessagesForOneRecipient() {
        assertEquals(TEXT_2 + "\n" + TEXT_5 + "\n", manager.searchByRecipient("+27838884567"));
    }

    @Test
    void disregardedMessagesAreNotFoundBySearchingARecipient() {
        assertEquals("No messages found for recipient +27834484567.", manager.searchByRecipient("+27834484567"));
    }

    @Test
    void deleteAMessageUsingItsHash() {
        assertEquals("Message: \"" + TEXT_2 + "\" successfully deleted.", manager.deleteByHash("20:1:WHERETIME"));

        assertEquals(4, manager.getTotalMessages());
        assertEquals(1, manager.getStoredCount());
        assertEquals(TEXT_5, manager.getStoredMessage(0));                  // moved from index 1 to 0
        assertNull(manager.getStoredMessage(1));                            // old last slot cleared
        assertEquals("Message ID 2021222324 not found.", manager.searchByMessageID("2021222324"));
    }

    @Test
    void deleteWithAnUnknownHashChangesNothing() {
        assertEquals("Message hash 99:9:NOPE not found.", manager.deleteByHash("99:9:NOPE"));
        assertEquals(5, manager.getTotalMessages());
    }

    @Test
    void sentMessagesCannotBeDeletedFromTheStoredMessagesMenu() {
        assertEquals("Message hash 10:0:DIDCAKE not found.", manager.deleteByHash("10:0:DIDCAKE"));
    }

    @Test
    void storedReportShowsIdHashRecipientAndMessage() {
        String expected = "STORED MESSAGES REPORT\n"
                        + "----------\n"
                        + "Message ID: 2021222324\n"
                        + "Message Hash: 20:1:WHERETIME\n"
                        + "Recipient: +27838884567\n"
                        + "Message: " + TEXT_2 + "\n"
                        + "----------\n"
                        + "Message ID: 5051525354\n"
                        + "Message Hash: 50:4:OKYOU\n"
                        + "Recipient: +27838884567\n"
                        + "Message: " + TEXT_5 + "\n"
                        + "----------\n"
                        + "Total: 2";
        assertEquals(expected, manager.displayStoredReport());
    }

    @Test
    void sentReportShowsHashRecipientAndMessage() {
        String report = manager.displaySentReport();
        assertTrue(report.contains("Message Hash: 10:0:DIDCAKE"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message Hash: 08:3:ITTIME"));
        assertTrue(report.endsWith("Total: 2"));
    }

    @Test
    void storedMessagesAreReadFromJsonIntoTheArrays() throws IOException {
                String file = tempDir.resolve("stored.json").toString();
        Message[] toStore = {
            new Message("2021222324", 1, "+27838884567", TEXT_2),
            new Message("5051525354", 4, "+27838884567", TEXT_5)
        };
        Message.writeStoredMessages(toStore, file);

        MessageManager fresh = new MessageManager();
        assertEquals("2 stored message(s) loaded from " + file, fresh.loadStoredFromJson(file));
        assertEquals(2, fresh.getStoredCount());
        assertEquals(TEXT_2, fresh.getStoredMessage(0));
        assertEquals("50:4:OKYOU", fresh.getMessageHash(1));
    }

    @Test
    void loadingAMissingFileLoadsNothing() {
        MessageManager fresh = new MessageManager();
        String file = tempDir.resolve("missing.json").toString();
        assertEquals("0 stored message(s) loaded from " + file, fresh.loadStoredFromJson(file));
        assertEquals(0, fresh.getStoredCount());
    }

    @Test
    void deletingAStoredMessageUpdatesTheJsonFile() throws IOException {
        String file = tempDir.resolve("afterDelete.json").toString();
        manager.deleteByHash("20:1:WHERETIME");
        manager.saveStoredToJson(file);

        Message[] fromFile = Message.readStoredMessages(file);
        assertEquals(1, fromFile.length);
        assertEquals(TEXT_5, fromFile[0].getMessageText());
    }
}