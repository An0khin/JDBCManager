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
                "postgres"
        );
    }

    @AfterEach
    public void clearTable() {
        jdbcManager.clearTable(tableName);
    }

    @Test
    public void insertRowToTable() {
        List<String> row = List.of("David", "Brok", "M");

        jdbcManager.insertRowToTable(row, tableName);

        Assertions.assertEquals(List.of(row), jdbcManager.getAll(tableName));
    }

    @Test
    public void deleteRowFromTable() {
        List<String> row1 = List.of("David", "Brok", "M");
        List<String> row2 = List.of("Billy", "Flute", "M");

        jdbcManager.insertRowToTable(row1, tableName);
        jdbcManager.insertRowToTable(row2, tableName);

        Assertions.assertEquals(List.of(row1, row2), jdbcManager.getAll(tableName));

        jdbcManager.deleteRowFromTable(row1, tableName);

        Assertions.assertEquals(List.of(row2), jdbcManager.getAll(tableName));
    }

    @Test
    public void editRowFromTable() {
        List<String> row1 = List.of("David", "Brok", "M");
        List<String> row2 = List.of("Billy", "Flute", "M");

        jdbcManager.insertRowToTable(row1, tableName);
        jdbcManager.insertRowToTable(row2, tableName);

        Assertions.assertEquals(List.of(row1, row2), jdbcManager.getAll(tableName));

        List<String> newRow1 = List.of("David", "Brok", "F");

        jdbcManager.editRowFromTable(row1, newRow1, tableName);

        Assertions.assertEquals(List.of(row2, newRow1), jdbcManager.getAll(tableName));
    }
}
