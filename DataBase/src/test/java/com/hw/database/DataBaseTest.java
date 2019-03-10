package com.hw.database;

import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseTest {

    private DataBase dataBase;

    @BeforeEach
    void init() throws SQLException {
        dataBase = new DataBase();
        dataBase.clear();
    }

    @Test
    void dataAfterClosingSizeTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Dasha", "88124386031");
        var anotherDataBase = new DataBase();
        assertEquals(2, anotherDataBase.getAll().size());
    }

    @Test
    void dataAfterClosingValuesTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Dasha", "88124386031");
        var anotherDataBase = new DataBase();
        assertEquals(1, anotherDataBase.getNumbers("Sasha").size());
        assertEquals("8 (812) 678-95-26", anotherDataBase.getNumbers("Sasha").get(0));
        assertEquals(1, anotherDataBase.getNumbers("Dasha").size());
        assertEquals("88124386031", anotherDataBase.getNumbers("Dasha").get(0));
    }

    @Test
    void simpleAddTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Dasha", "88124386031");
        var all = dataBase.getAll();
        assertEquals(List.of(new DataBase.PhonebookPair("Sasha", "8 (812) 678-95-26"),
                new DataBase.PhonebookPair("Dasha", "88124386031")), all);
    }

    @Test
    void severalNumbersTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Sasha", "88124386031");
        var all = dataBase.getNumbers("Sasha");
        assertEquals(List.of("8 (812) 678-95-26", "88124386031"), all);
    }

    @Test
    void severalNamesTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Dasha", "8 (812) 678-95-26");
        assertEquals(List.of("Sasha", "Dasha"), dataBase.getNames("8 (812) 678-95-26"));
    }

    @Test
    void updateNameTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.updateName("Sasha", "Dasha", "8 (812) 678-95-26");
        var names = dataBase.getNames("8 (812) 678-95-26");
        assertEquals(1, names.size());
        assertEquals("Dasha", names.get(0));
    }

    @Test
    void updateNumberTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.updateNumber("Sasha", "8 (812) 678-95-26", "88124386031");
        assertEquals(0, dataBase.getNames("8 (812) 678-95-26").size());
        assertEquals("88124386031", dataBase.getNumbers("Sasha").get(0));
    }

    @Test
    void getNumbersTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Sasha", "88124386031");
        dataBase.add("sasha", "+79119405740");
        assertEquals(List.of("8 (812) 678-95-26", "88124386031"), dataBase.getNumbers("Sasha"));
    }

    @Test
    void getNumbersNullTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Sasha", "88124386031");
        dataBase.add("sasha", "+79119405740");
        assertEquals(List.of(), dataBase.getNumbers("Dasha"));
    }

    @Test
    void getNamesTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Dasha", "8 (812) 678-95-26");
        dataBase.add("sasha", "+8 (812) 678-95-26");
        assertEquals(List.of("Sasha", "Dasha"), dataBase.getNames("8 (812) 678-95-26"));
    }

    @Test
    void getNamesNullTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Dasha", "8 (812) 678-95-26");
        dataBase.add("sasha", "+8 (812) 678-95-26");
        assertEquals(List.of(), dataBase.getNames("88124386031"));
    }

    @Test
    void simpleDeleteTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.delete("Dasha", "88124386031");
        assertEquals(1, dataBase.getAll().size());
    }

    @Test
    void severalNamesDeleteTest() throws SQLException {
        dataBase.add("Sasha", "8 (812) 678-95-26");
        dataBase.add("Sasha", "88124386031");
        dataBase.delete("Sasha", "88124386031");
        assertEquals(List.of("8 (812) 678-95-26"), dataBase.getNumbers("Sasha"));
    }

    @Test
    void severalNumbersDeleteTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Dasha", "88124386031");
        dataBase.delete("Sasha", "88124386031");
        assertEquals(1, dataBase.getAll().size());
    }

    @Test
    void getAllSimpleTest() throws SQLException {
        assertEquals(List.of(), dataBase.getAll());
    }

    @Test
    void getAllDifferentTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Dasha", "+79119405740");
        dataBase.add("Masha", "8 (812) 678-95-26");
        assertEquals(3, dataBase.getAll().size());
    }

    @Test
    void getAllEqualNamesTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Sasha", "+79119405740");
        dataBase.add("Sasha", "8 (812) 678-95-26");
        assertEquals(3, dataBase.getAll().size());
    }

    @Test
    void getAllEqualNumbersTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Dasha", "88124386031");
        dataBase.add("Masha", "88124386031");
        assertEquals(3, dataBase.getAll().size());
    }

    @Test
    void getAllSamePairTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Sasha", "88124386031");
        assertEquals(1, dataBase.getAll().size());
    }

    @Test
    void getAllAfterRemoveTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Dasha", "+79119405740");
        dataBase.add("Masha", "8 (812) 678-95-26");
        dataBase.delete("Sasha", "88124386031");
        assertEquals(2, dataBase.getAll().size());
    }

    @Test
    void getAllAfterRemoveNonexistingTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Dasha", "+79119405740");
        dataBase.add("Masha", "8 (812) 678-95-26");
        dataBase.delete("Pasha", "6");
        assertEquals(3, dataBase.getAll().size());
    }

    @Test
    void getAllEqualsTest() throws SQLException {
        dataBase.add("Sasha", "88124386031");
        dataBase.add("Dasha", "+79119405740");
        dataBase.add("Masha", "8 (812) 678-95-26");
        assertEquals(List.of(new DataBase.PhonebookPair("Sasha", "88124386031"),
                            new DataBase.PhonebookPair("Dasha", "+79119405740"),
                            new DataBase.PhonebookPair("Masha", "8 (812) 678-95-26")),
                            dataBase.getAll());
    }
}