package com.home;

import java.util.List;

public interface JDBCManager {
    List<List<String>> getAll(String tableName);

    boolean insertRowToTable(List<String> row, String tableName);

    boolean deleteRowFromTable(List<String> row, String tableName);

    boolean editRowFromTable(List<String> oldRow, List<String> newRow, String tableName);

    boolean clearTable(String tableName);
}
