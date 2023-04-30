package com.home;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultJDBCManager implements JDBCManager {

    private final String login;
    private final String password;
    private final String url;

    public DefaultJDBCManager(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public List<List<String>> getAll(String tableName) {
        List<List<String>> result = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, login, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s", tableName));

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                List<String> row = new ArrayList<>();
                int columnsNumber = resultSet.getMetaData().getColumnCount();

                for(int i = 1; i <= columnsNumber; i++) {
                    row.add(resultSet.getString(i));
                }

                result.add(row);
            }

            resultSet.close();
        } catch(SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean insertRowToTable(List<String> row, String tableName) {
        try(Connection connection = DriverManager.getConnection(url, login, password)) {
            int columnsNumber = row.size();

            PreparedStatement preparedStatement =
                    connection.prepareStatement(prepareInsertRequest(tableName, columnsNumber));

            for(int i = 1; i <= columnsNumber; i++) {
                preparedStatement.setString(i, row.get(i - 1));
            }

            boolean result = preparedStatement.execute();

            return result;

        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRowFromTable(List<String> row, String tableName) {
        try(Connection connection = DriverManager.getConnection(url, login, password)) {
            int columnsNumber = row.size();

            PreparedStatement preparedStatement = connection.prepareStatement(prepareDeleteRequest(tableName));

            for(int i = 1; i <= columnsNumber; i++) {
                preparedStatement.setString(i, row.get(i - 1));
            }

            boolean result = preparedStatement.execute();

            return result;

        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editRowFromTable(List<String> oldRow, List<String> newRow, String tableName) {
        try(Connection connection = DriverManager.getConnection(url, login, password)) {
            int columnsNumber = oldRow.size();

            PreparedStatement preparedStatement = connection.prepareStatement(prepareUpdateRequest(tableName));

            for(int i = 1; i <= columnsNumber; i++) {
                preparedStatement.setString(i, newRow.get(i - 1));

                preparedStatement.setString(i + columnsNumber, oldRow.get(i - 1));
            }

            boolean result = preparedStatement.execute();

            return result;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean clearTable(String tableName) {
        try(Connection connection = DriverManager.getConnection(url, login, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("DELETE FROM %s;", tableName));

            boolean result = preparedStatement.execute();

            return result;
        } catch(SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private List<String> getColumnsNamesFromTable(String tableName) {
        List<String> columnNames = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, login, password)) {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM %s;", tableName));
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();

            for(int i = 1; i <= columnsNumber; i++) { //columns' names to List
                columnNames.add(resultSetMetaData.getColumnName(i));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return columnNames;
    }


    private String prepareInsertRequest(String tableName, int columnsNumber) {
        StringBuilder requestBuilder = new StringBuilder(String.format("INSERT INTO %s VALUES(", tableName));

        requestBuilder.append("?,".repeat(columnsNumber));

        requestBuilder.deleteCharAt(requestBuilder.length() - 1);
        requestBuilder.append(");");

        return requestBuilder.toString();
    }

    private String prepareDeleteRequest(String tableName) {
        List<String> columnsNames = getColumnsNamesFromTable(tableName);

        String requestBuilder = String.format("DELETE FROM %s ", tableName) + buildWhereSequence(columnsNames) +
                ";";

        return requestBuilder;
    }

    private String prepareUpdateRequest(String tableName) {
        List<String> columnsNames = getColumnsNamesFromTable(tableName);

        String requestBuilder = String.format("UPDATE %s ", tableName) + buildSetSequence(columnsNames) +
                " " +
                buildWhereSequence(columnsNames) +
                ";";

        return requestBuilder;
    }

    private String buildWhereSequence(List<String> fields) {
        StringBuilder whereSequenceBuilder = new StringBuilder("WHERE ");

        int size = fields.size();

        for(int i = 0; i < size; i++) {
            whereSequenceBuilder.append(String.format("%s = ? ", fields.get(i)));
            if(i != size - 1) {
                whereSequenceBuilder.append("AND ");
            }
        }

        return whereSequenceBuilder.toString();
    }

    private String buildSetSequence(List<String> fields) {
        StringBuilder setSequenceBuilder = new StringBuilder("SET ");

        int size = fields.size();

        for(int i = 0; i < size; i++) {
            setSequenceBuilder.append(String.format("%s = ?", fields.get(i)));
            if(i != size - 1) {
                setSequenceBuilder.append(", ");
            }
        }

        return setSequenceBuilder.toString();
    }
}
