package com.hw.database;

import org.jetbrains.annotations.*;

import java.sql.*;
import java.util.*;

/**
 * Structure stores unique name-number pairs via SQL-table.
 */
public class DataBase {
    private final Connection connection;
    private static final String table = "phonebook";

    /**
     * (String, String) pair for convenience, returning table elements.
     */
    public static class PhonebookPair {
        private final String name;
        private final String number;

        public PhonebookPair(@NotNull String name, @NotNull String number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Contact: " + name + " this number : " + number;
        }

        @Override
        public boolean equals(Object other){
            if (other instanceof PhonebookPair) {
                var casted = (PhonebookPair) other;
                return name.equals(casted.name) && number.equals(casted.number);
            }
            return false;
        }
    }

    /**
     * Creates new table named phonebook.
     */
    public DataBase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory");
        try (Statement statement = connection.createStatement()) {
            statement.execute("drop table if exists " + table);
            statement.execute("create table if not exists " + table + "(name text, number text)");
        }
    }

    /**
     * Adds new pair to the talbe.
     * If table already contains such pair, does nothing.
     * @param name name of the pair
     * @param number number of the pair
     */
    public void add(@NotNull String name, @NotNull String number) throws SQLException {
        if (getNumbers(name).contains(number)) {
            return;
        }
        final String sql = "insert into " + table + "(name, number) values(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, number);
            statement.executeUpdate();
        }
    }

    /**
     * Changes name in given pair.
     */
    public void updateName(@NotNull String name, @NotNull String newName, @NotNull String number) throws SQLException {
        final String sql = "update " +  table + " set name = ? where name = ? and number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, name);
            statement.setString(3, number);
            statement.executeUpdate();
        }
    }

    /**
     * Changes number in given pair.
     */
    public void updateNumber(String name, String number, String newNumber) throws SQLException {
        final String sql = "update " + table + " set number = ? where name = ? and number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newNumber);
            statement.setString(2, name);
            statement.setString(3, number);
            statement.executeUpdate();
        }
    }

    /**
     * Returns the list of numbers contained in the table by given name.
     */
    public List<String> getNumbers(String name) throws SQLException {
        final String sql = "select name, number from " + table + " where name = ?";
        List<String> numbers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    numbers.add(resultSet.getString("number"));
                }
            }
        }
        return numbers;
    }

    /**
     * Returns the list of names contained in the table by given number.
     */
    public List<String> getNames(String number) throws SQLException {
        final String sql = "select name, number from " + table + " where number = ?";
        List<String> names = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql); ) {
            statement.setString(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    names.add(resultSet.getString("name"));
                }
            }

        }
        return names;
    }

    /**
     * Removes pair from the table.
     * @param name name of the pair
     * @param number number of the pair
     */
    public void delete(String name, String number) throws SQLException {
        final String sql = "delete from " + table + " where name = ? and number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, number);
            statement.executeUpdate();
        }
    }

    /**
     * Returns list of all name-number pairs in the table.
     */
    public List<PhonebookPair> getAll() throws SQLException {
        final String sql = "select * from " + table;
        List<PhonebookPair> all = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                all.add(new PhonebookPair(resultSet.getString("name"), resultSet.getString("number")));
            }
        }
        return all;
    }
}
