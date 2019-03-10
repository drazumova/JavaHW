package com.hw.database;

import org.jetbrains.annotations.*;

import java.sql.*;
import java.util.*;

/**
 * Structure stores unique name-number pairs via SQL-table.
 */
public class DataBase {
    private final Connection connection;
    private static final String tableWithNames = "namesTable";
    private static final String tableWithNumbers = "numbersTable";
    private static final String tableWithPairs = "phonebook";

    /**
     * (String, String) pair for convenience, returning table elements.
     */
    public static class PhonebookPair {
        @NotNull
        private final String name;
        @NotNull
        private final String number;

        public PhonebookPair(@NotNull String name, @NotNull String number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public String toString() {
            return "Contact: " + name + " with number : " + number;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof PhonebookPair) {
                var casted = (PhonebookPair) other;
                return name.equals(casted.name) && number.equals(casted.number);
            }
            return false;
        }
    }

    /**
     * Creates new tables if they do not exist named phonebook, namesTable and numbersTable.
     * Creates src/main/resources/phoonebook.db to save data.
     */
    public DataBase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/phoonebook.db");
        try (Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists "
                    + tableWithNames
                    + "(id integer primary key, name text)");
            statement.execute("create table if not exists "
                    + tableWithNumbers
                    + "(id integer primary key , number text)");
            statement.execute("create table if not exists "
                    + tableWithPairs
                    + "(name integer , number integer)");
        }
    }

    private Integer getIdByName(@NotNull String name) throws SQLException {
        final String sql = "select id, name from " + tableWithNames + " where name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return null;
                }
            }
        }
    }

    private Integer getIdByNumber(@NotNull String number) throws SQLException {
        final String sql = "select id, number from " + tableWithNumbers + " where number = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    return null;
                }
            }
        }
    }

    private String getNameById(int id) throws SQLException {
        final String sql = "select id, name from " + tableWithNames + " where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                } else {
                    return null;
                }
            }
        }
    }

    private String getNumberById(int id) throws SQLException {
        final String sql = "select id, number from " + tableWithNumbers + " where id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("number");
                } else {
                    return null;
                }
            }
        }
    }

    private Integer addName(String name) throws SQLException {
        final String sql = "insert into " + tableWithNames + "(name) values(?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
        }
        return getIdByName(name);
    }

    private Integer addNumber(String number) throws SQLException {
        final String sql = "insert into " + tableWithNumbers + "(number) values(?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, number);
            statement.executeUpdate();
        }
        return getIdByNumber(number);
    }

    private void tryRemoveName(String name) throws SQLException {
        if (getNumbers(name).isEmpty()) {
            final String sql = "delete from " + tableWithNames + " where name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.executeUpdate();
            }
        }
    }

    private void tryRemoveNumber(String number) throws SQLException {
        if (getNames(number).isEmpty()) {
            final String sql = "delete from " + tableWithNumbers + " where number = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, number);
                statement.executeUpdate();
            }
        }
    }

    /**
     * Adds new pair to the table.
     * If table already contains such pair, does nothing.
     * @param name name of the pair
     * @param number number of the pair
     */
    public void add(@NotNull String name, @NotNull String number) throws SQLException {
        var nameId = addName(name);
        var numberId = addNumber(number);

        if (getNumbers(name).contains(number)) {
            return;
        }

        final String sql = "insert into " + tableWithPairs + "(name, number) values(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nameId);
            statement.setInt(2, numberId);
            statement.executeUpdate();
        }
    }

    /**
     * Changes name in given pair.
     */
    public void updateName(@NotNull String name, @NotNull String newName, @NotNull String number) throws SQLException {
        var numberId = getIdByNumber(number);
        var oldNameId = getIdByName(name);
        var newNameId = addName(newName);

        if (oldNameId == null || numberId == null) {
            return;
        }

        final String sql = "update " +  tableWithPairs + " set name = ? where name = ? and number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newNameId);
            statement.setInt(2, oldNameId);
            statement.setInt(3, numberId);
            statement.executeUpdate();
        }
    }

    /**
     * Changes number in given pair.
     */
    public void updateNumber(String name, String number, String newNumber) throws SQLException {
        var nameId = getIdByName(name);
        var oldNumberId = getIdByNumber(number);
        var newNumberId = addNumber(newNumber);

        if (nameId == null || oldNumberId == null) {
            return;
        }

        final String sql = "update " + tableWithPairs + " set number = ? where name = ? and number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newNumberId);
            statement.setInt(2, nameId);
            statement.setInt(3, oldNumberId);
            statement.executeUpdate();
        }
    }

    /**
     * Returns the list of numbers contained in the table by given name.
     */
    public List<String> getNumbers(String name) throws SQLException {
        final String sql = "select name, number from " + tableWithPairs + " where name = ?";
        List<String> numbers = new ArrayList<>();
        var id = getIdByName(name);
        if (id == null) {
            return numbers;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    numbers.add(getNumberById(resultSet.getInt("number")));
                }
            }
        }
        return numbers;
    }

    /**
     * Returns the list of names contained in the table by given number.
     */
    public List<String> getNames(String number) throws SQLException {
        var id = getIdByNumber(number);
        final String sql = "select name, number from " + tableWithPairs + " where number = ?";
        List<String> names = new ArrayList<>();
        if (id == null) {
            return names;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var name = getNameById(resultSet.getInt("name"));
                    names.add(name);
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
        var nameId = getIdByName(name);
        var numberId = getIdByNumber(number);
        if (numberId == null || nameId == null) {
            return;
        }
        final String sql = "delete from " + tableWithPairs + " where name = ? and number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nameId);
            statement.setInt(2, numberId);
            statement.executeUpdate();
        }

        tryRemoveName(name);
        tryRemoveNumber(number);
    }

    /**
     * Returns list of all name-number pairs in the table.
     */
    public List<PhonebookPair> getAll() throws SQLException {
        final String sql = "select * from " + tableWithPairs;
        List<PhonebookPair> all = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var name = getNameById(resultSet.getInt("name"));
                var number = getNumberById(resultSet.getInt("number"));
                all.add(new PhonebookPair(name, number));
            }
        }
        return all;
    }

    public void clear() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("delete from " + tableWithNames);
            statement.execute("delete from " + tableWithNumbers);
            statement.execute("delete from " + tableWithPairs);
        }
    }
}
