package com.hw.database;

import java.nio.charset.*;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String... args) {
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            DataBase dataBase = new DataBase();
            String name;
            String number;
            boolean going = true;
            while (going) {
                int action = Integer.parseInt(scanner.nextLine());
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
