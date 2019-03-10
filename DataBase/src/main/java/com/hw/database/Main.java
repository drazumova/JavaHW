package com.hw.database;

import java.nio.charset.*;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String... args) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            DataBase dataBase = new DataBase();
            final String greeting = "Welcome to the incredible console phonebook!\n" +
                    "It contains unique (name, number) pairs.\n" +
                    "All input is separated by line breaks.\n";
            final String usage = "To exit enter 0\n" +
                    "To add (name, number) pair to phonebook enter 1 <name> <number>\n" +
                    "To get all numbers by name enter 2 <name>\n" +
                    "To get all names by number enter 3 <number>\n" +
                    "To delete (name, number) pair from phonebook enter 4 <name> <number>\n" +
                    "To update name in (name, number) pair enter 5 <name> <number> <new name>\n" +
                    "To update number in (name, number) pair enter 6 <name> <number> <new number>\n" +
                    "To get all (name, number) pairs in the phonebook enter 7\n" +
                    "To get usage again enter 8\n";
            System.out.println(greeting + "\n" + usage);
            boolean going = true;
            while (going) {
                int action = Integer.parseInt(scanner.nextLine());
                String name;
                String number;
                switch (action) {
                    case 0:
                        going = false;
                        break;
                    case 1:
                        name = scanner.nextLine();
                        number = scanner.nextLine();
                        dataBase.add(name, number);
                        break;
                    case 2:
                        name = scanner.nextLine();
                        List<String> numbers = dataBase.getNumbers(name);
                        for (var current : numbers) {
                            System.out.println(current);
                        }
                        break;
                    case 3:
                        number = scanner.nextLine();
                        List<String> names = dataBase.getNames(number);
                        for (var current : names) {
                            System.out.println(current);
                        }
                        break;
                    case 4:
                        name = scanner.nextLine();
                        number = scanner.nextLine();
                        dataBase.delete(name, number);
                        break;
                    case 5:
                        name = scanner.nextLine();
                        number = scanner.nextLine();
                        String newName = scanner.nextLine();
                        dataBase.updateName(name, newName, number);
                        break;
                    case 6:
                        name = scanner.nextLine();
                        number = scanner.nextLine();
                        String newNumber = scanner.nextLine();
                        dataBase.updateNumber(name, number, newNumber);
                        break;
                    case 7:
                        List<DataBase.PhonebookPair> all = dataBase.getAll();
                        for (var current : all) {
                            System.out.println(current);
                        }
                        break;
                    case 8:
                        System.out.println(usage);
                        break;
                    default:
                        System.out.println("Wrong command");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("AAAAAAAAAAAAAAA " + e.getMessage());
        }

    }
}
