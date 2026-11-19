package com.mycompany.progassignment1;

import java.util.Scanner;

/**
 * @author letlhogonolokgosiemang
 */
public class QuickChat {

    public static void start(Scanner scanner, String senderName) {
        System.out.println("\nWelcome to QuickChat.");

        // PART 3: the manager keeps every message in arrays; stored messages are read from the JSON file into them
        MessageManager manager = new MessageManager();
        System.out.println(manager.loadStoredFromJson(Message.JSON_FILE));

        int numberOfMessages = askNumberOfMessages(scanner);
        boolean messagesEntered = false;

        // while loop: the menu keeps running until the user quits
        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesEntered) {
                        System.out.println("You have already entered all " + numberOfMessages + " messages.");
                    } else {
                        sendMessages(scanner, numberOfMessages, manager);
                        messagesEntered = true;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                case "4":
                    showStoredMessagesMenu(scanner, manager, senderName);
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3 or 4.");
            }
        }

        System.out.println("Thank you for using QuickChat. Goodbye.");
    }

    // for loop: runs once for every message the user said they would enter
    private static void sendMessages(Scanner scanner, int numberOfMessages, MessageManager manager) {
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numberOfMessages + " ---");

            String recipient = askRecipient(scanner);
            String text = askMessageText(scanner);

            // the loop counter i is the message number used in the hash
            Message message = new Message(i, recipient, text);
            System.out.println(message.getMessageIDStatus());

            int choice = askSendChoice(scanner);
            String result = message.sentMessage(choice);
            System.out.println(result);

            // PART 3: put the message into the correct array
            if (choice == 1) {
                manager.addMessage(message, MessageManager.FLAG_SENT);
                System.out.println("\n" + message.getDetails());
            } else if (choice == 2) {
                while (!scanner.nextLine().trim().equals("0")) {
                    System.out.println("Press 0 to delete the message.");
                }
                manager.addMessage(message, MessageManager.FLAG_DISREGARD);
                System.out.println("Message deleted.");
            } else if (result.equals("Message successfully stored.")) {
                manager.addMessage(message, MessageManager.FLAG_STORED);
            }
        }

        System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
    }

    // PART 3: everything we can do with the stored messages
    private static void showStoredMessagesMenu(Scanner scanner, MessageManager manager, String senderName) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\nSTORED MESSAGES");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to the main menu");
            System.out.print("Option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.println(manager.displaySenderAndRecipient(senderName));
                    break;
                case "2":
                    System.out.println("Longest stored message: " + manager.longestStoredMessage());
                    break;
                case "3":
                    System.out.print("Message ID: ");
                    System.out.println(manager.searchByMessageID(scanner.nextLine().trim()));
                    break;
                case "4":
                    System.out.print("Recipient cell number: ");
                    System.out.println(manager.searchByRecipient(scanner.nextLine().trim()));
                    break;
                case "5":
                    System.out.print("Message hash: ");
                    String hash = scanner.nextLine().trim().toUpperCase();
                    String deleteResult = manager.deleteByHash(hash);
                    System.out.println(deleteResult);
                    if (deleteResult.endsWith("successfully deleted.")) {
                        manager.saveStoredToJson(Message.JSON_FILE);   // keep the file in step with the array
                    }
                    break;
                case "6":
                    System.out.println(manager.displayStoredReport());
                    break;
                case "7":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number from 1 to 7.");
            }
        }
    }

    private static int askNumberOfMessages(Scanner scanner) {
        while (true) {
            System.out.print("How many messages do you want to enter? ");
            try {
                int count = Integer.parseInt(scanner.nextLine().trim());
                if (count > 0) {
                    return count;
                }
            } catch (NumberFormatException e) {
                // fall through to the message below
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }

    private static String askRecipient(Scanner scanner) {
        while (true) {
            System.out.print("Recipient cell number (e.g. +27718693002): ");
            String number = scanner.nextLine().trim();
            System.out.println(Message.recipientStatus(number));
            if (Message.isValidRecipient(number)) {
                return number;
            }
        }
    }

    private static String askMessageText(Scanner scanner) {
        while (true) {
            System.out.print("Message (max 250 characters): ");
            String text = scanner.nextLine().trim();
            if (text.isEmpty()) {
                System.out.println("Please enter a message.");
            } else {
                System.out.println(Message.lengthStatus(text));
                if (text.length() <= Message.MAX_LENGTH) {
                    return text;
                }
            }
        }
    }

    private static int askSendChoice(Scanner scanner) {
        while (true) {
            System.out.println("What would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choice: ");
            String input = scanner.nextLine().trim();
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return Integer.parseInt(input);
            }
            System.out.println("Invalid choice. Please choose 1, 2 or 3.");
        }
    }
}