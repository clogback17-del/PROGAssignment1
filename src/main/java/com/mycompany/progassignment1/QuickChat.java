/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.progassignment1;

import java.util.Scanner;
/**
 *
 * @author letlhogonolokgosiemang
 */
public class QuickChat {
    
    public static void start(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");

        int numberOfMessages = askNumberOfMessages(scanner);
        boolean messagesEntered = false;

        // while loop: the menu keeps running until the user quits
        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesEntered) {
                        System.out.println("You have already entered all " + numberOfMessages + " messages.");
                    } else {
                        sendMessages(scanner, numberOfMessages);
                        messagesEntered = true;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
            }
        }

        System.out.println("Thank you for using QuickChat. Goodbye.");
    }

    // for loop: runs once for every message the user said they would enter
    private static void sendMessages(Scanner scanner, int numberOfMessages) {
        for (int i = 0; i < numberOfMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numberOfMessages + " ---");

            String recipient = askRecipient(scanner);
            String text = askMessageText(scanner);

            // the loop counter i is the message number used in the hash
            Message message = new Message(i, recipient, text);
            System.out.println(message.getMessageIDStatus());

            int choice = askSendChoice(scanner);
            System.out.println(message.sentMessage(choice));

            if (choice == 1) {
                System.out.println("\n" + message.getDetails());
            } else if (choice == 2) {
                while (!scanner.nextLine().trim().equals("0")) {
                    System.out.println("Press 0 to delete the message.");
                }
                System.out.println("Message deleted.");
            }
        }

        System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
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
