package com.home;

import java.util.List;

public interface JDBCManager {
    List<List<String>> getAll();

    boolean insertRowToTable(List<String> row);

    boolean deleteRowFromTable(List<String> row);

    boolean editRowFromTable(List<String> oldRow, List<String> newRow);

    boolean clearTable();
}
