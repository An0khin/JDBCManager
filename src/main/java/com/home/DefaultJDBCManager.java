package com.home;

import com.home.service.DefaultSqlPreparer;
import com.home.service.DefaultStatementPreparer;
import com.home.service.SqlPreparer;
import com.home.service.StatementPreparer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultJDBCManager implements JDBCManager {
    private StatementPreparer statementPreparer;

    public DefaultJDBCManager(String url, String login, String password, String tableName) {
        initStatementPreparer(url, login, password, tableName);
    }

    private void initStatementPreparer(String url, String login, String password, String tableName) {
        this.statementPreparer = new DefaultStatementPreparer(
                url,
                login,
                password,
                tableName
        );

        ((DefaultStatementPreparer) this.statementPreparer).setSqlPreparer(buildSqlPreparer(tableName));
    }

    private SqlPreparer buildSqlPreparer(String tableName) {
        return new DefaultSqlPreparer(
                getColumnsNamesFromTable(),
                tableName
        );
    }

    @Override
    public List<List<String>> getAll() {
        try(PreparedStatement preparedStatement = statementPreparer.prepareGetAllStatement()) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return allRows(resultSet);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            return Collections.emptyList();
        }
    }

    private List<List<String>> allRows(final ResultSet resultSet) throws SQLException {
        List<List<String>> allRows = new ArrayList<>();

        while(resultSet.next()) {
            List<String> row = fillRow(resultSet);
            allRows.add(row);
        }

        return allRows;
    }

    private List<String> fillRow(final ResultSet resultSet) throws SQLException {
        List<String> row = new ArrayList<>();
        int columnsNumber = resultSet.getMetaData().getColumnCount();

        for(int i = 1; i <= columnsNumber; i++) {
            row.add(resultSet.getString(i));
        }

        return row;
    }


    @Override
    public boolean insertRowToTable(List<String> row) {
        try(PreparedStatement preparedStatement = statementPreparer.prepareInsertStatement()) {

            for(int i = 1; i <= row.size(); i++) {
                preparedStatement.setString(i, row.get(i - 1));
            }

            return preparedStatement.execute();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteRowFromTable(List<String> row) {
        try(PreparedStatement preparedStatement = statementPreparer.prepareDeleteStatement()) {

            for(int i = 1; i <= row.size(); i++) {
                preparedStatement.setString(i, row.get(i - 1));
            }

            return preparedStatement.execute();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean editRowFromTable(List<String> oldRow, List<String> newRow) {
        try(PreparedStatement preparedStatement = statementPreparer.prepareUpdateStatement()) {
            int columnsNumber = oldRow.size();

            for(int i = 1; i <= columnsNumber; i++) {
                preparedStatement.setString(i, newRow.get(i - 1));

                preparedStatement.setString(i + columnsNumber, oldRow.get(i - 1));
            }

            return preparedStatement.execute();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean clearTable() {
        try(PreparedStatement preparedStatement = statementPreparer.prepareClearStatement()) {
            return preparedStatement.execute();
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    private List<String> getColumnsNamesFromTable() {
        try(PreparedStatement preparedStatement = statementPreparer.prepareGetColumnsNamesStatement()) {
            List<String> columnNames = new ArrayList<>();

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();

            for(int i = 1; i <= columnsNumber; i++) { //columns' names to List
                columnNames.add(resultSetMetaData.getColumnName(i));
            }

            return columnNames;
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            return Collections.emptyList();
        }
    }
}
