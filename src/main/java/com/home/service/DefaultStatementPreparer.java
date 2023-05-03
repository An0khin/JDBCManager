package com.home.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultStatementPreparer implements StatementPreparer {
    private final String url;
    private final String login;
    private final String password;
    private final String tableName;
    private SqlPreparer sqlPreparer;

    public DefaultStatementPreparer(String url, String login, String password, String tableName) {
        this.url = url;
        this.login = login;
        this.password = password;
        this.tableName = tableName;
    }

    public void setSqlPreparer(SqlPreparer sqlPreparer) {
        this.sqlPreparer = sqlPreparer;
    }

    @Override
    public PreparedStatement prepareGetColumnsNamesStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        String getColumnsNamesSql = String.format("SELECT * FROM %s;", tableName);
        return connection.prepareStatement(getColumnsNamesSql);
    }

    @Override
    public PreparedStatement prepareClearStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        String clearSql = String.format("DELETE FROM %s;", tableName);
        return connection.prepareStatement(clearSql);
    }

    @Override
    public PreparedStatement prepareUpdateStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        String updateSql = sqlPreparer.prepareUpdateRequest();
        return connection.prepareStatement(updateSql);
    }

    @Override
    public PreparedStatement prepareDeleteStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        String deleteSql = sqlPreparer.prepareDeleteRequest();
        return connection.prepareStatement(deleteSql);
    }

    @Override
    public PreparedStatement prepareInsertStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        String insertSql = sqlPreparer.prepareInsertRequest();
        return connection.prepareStatement(insertSql);
    }

    @Override
    public PreparedStatement prepareGetAllStatement() throws SQLException {
        Connection connection = DriverManager.getConnection(url, login, password);
        String getAllSql = String.format("SELECT * FROM %s", tableName);
        return connection.prepareStatement(getAllSql);
    }
}
