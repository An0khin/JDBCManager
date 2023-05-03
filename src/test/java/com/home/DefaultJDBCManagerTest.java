package com.home;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DefaultJDBCManagerTest {
    JDBCManager jdbcManager;
    String tableName = "test";

    public DefaultJDBCManagerTest() {
        jdbcManager = new DefaultJDBCManager(
                "jdbc:postgresql://localhost:5432/test_db",
                "postgres",
                "postgres",
                tableName
        );
    }

    @AfterEach
    public void clearTable() {
        jdbcManager.clearTable();
    }

    @Test
    public void insertRowToTable() {
        List<String> row = List.of("David", "Brok", "M");

        jdbcManager.insertRowToTable(row);

        Assertions.assertEquals(List.of(row), jdbcManager.getAll());
    }

    @Test
    public void deleteRowFromTable() {
        List<String> row1 = List.of("David", "Brok", "M");
        List<String> row2 = List.of("Billy", "Flute", "M");

        jdbcManager.insertRowToTable(row1);
        jdbcManager.insertRowToTable(row2);

        Assertions.assertEquals(List.of(row1, row2), jdbcManager.getAll());

        jdbcManager.deleteRowFromTable(row1);

        Assertions.assertEquals(List.of(row2), jdbcManager.getAll());
    }

    @Test
    public void editRowFromTable() {
        List<String> row1 = List.of("David", "Brok", "M");
        List<String> row2 = List.of("Billy", "Flute", "M");

        jdbcManager.insertRowToTable(row1);
        jdbcManager.insertRowToTable(row2);

        Assertions.assertEquals(List.of(row1, row2), jdbcManager.getAll());

        List<String> newRow1 = List.of("David", "Brok", "F");

        jdbcManager.editRowFromTable(row1, newRow1);

        Assertions.assertEquals(List.of(row2, newRow1), jdbcManager.getAll());
    }
}
